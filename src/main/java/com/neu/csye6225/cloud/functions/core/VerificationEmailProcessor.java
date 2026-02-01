package com.neu.csye6225.cloud.functions.core;

import com.google.gson.Gson;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import com.neu.csye6225.cloud.functions.dao.VerifyDao;
import com.neu.csye6225.cloud.functions.entity.EventArtifact;
import com.neu.csye6225.cloud.functions.entity.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificationEmailProcessor {

  private static final Logger logger = LoggerFactory.getLogger(VerificationEmailProcessor.class);

  private static final Gson gson = new Gson();

  private final String domain;
  private final String emailFrom;
  private final MailgunMessagesApi mailgun;

  public VerificationEmailProcessor() {
    this.domain = mustEnv("SENDER_DOMAIN");
    this.emailFrom = "no-reply@" + domain;

    String apiKey = mustEnv("MAILGUN_API_KEY");
    this.mailgun = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);
  }

  public void processJson(String eventArtifactJson) {
    if (eventArtifactJson == null || eventArtifactJson.isBlank()) {
      logger.warn("Empty payload");
      return;
    }

    EventArtifact eventArtifact = gson.fromJson(eventArtifactJson, EventArtifact.class);
    if (eventArtifact == null || eventArtifact.getUsername() == null || eventArtifact.getToken() == null) {
      logger.warn("Invalid JSON payload: {}", eventArtifactJson);
      return;
    }

    try {
      Verify verify = VerifyDao.getVerify(eventArtifact.getUsername(), eventArtifact.getToken());
      if (verify == null) {
        logger.error("Invalid username/token: {}", eventArtifact);
        return;
      }

      logger.info("Sending verification email: {}", eventArtifact);

      Message mail = Message.builder()
          .from(emailFrom)
          .to(eventArtifact.getUsername())
          .subject("CSYE6225 - Verify mail")
          .html(getHtmlContent(eventArtifact.getToken()))
          .build();

      MessageResponse resp = mailgun.sendMessage(domain, mail);
      logger.info("Mail response: {}", resp);

      VerifyDao.updateGeneratedTime(eventArtifact.getToken());

    } catch (Exception e) {
      logger.error("Processing failed", e);
    }
  }

  private String getHtmlContent(String token) {
    return "<html><body>"
        + "<h1>CSYE6225 - Verify mail</h1>"
        + "<p style=\"color:blue; font-size:30px;\">Click below link to verify your account</p>"
        + "<p style=\"font-size:30px;\">https://"
        + domain
        + "/v1/user/verify?token="
        + token
        + "</p>"
        + "</body></html>";
  }

  private static String mustEnv(String key) {
    String v = System.getenv(key);
    if (v == null || v.isBlank()) {
      throw new IllegalStateException("Missing env var: " + key);
    }
    return v;
  }
}
