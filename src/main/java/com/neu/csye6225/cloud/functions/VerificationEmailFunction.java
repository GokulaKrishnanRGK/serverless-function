package com.neu.csye6225.cloud.functions;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.gson.Gson;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import com.neu.csye6225.cloud.functions.dao.VerifyDao;
import com.neu.csye6225.cloud.functions.entity.EventArtifact;
import com.neu.csye6225.cloud.functions.entity.PubSubMessage;
import com.neu.csye6225.cloud.functions.entity.Verify;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificationEmailFunction implements BackgroundFunction<PubSubMessage> {

  private static final Logger logger = LoggerFactory.getLogger(VerificationEmailFunction.class);

  private static final String DOMAIN = System.getenv("SENDER_DOMAIN");
  private static final String EMAIL_FROM = "no-reply@" + DOMAIN;

  private static final MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(System.getenv("MAILGUN_API_KEY"))
      .createApi(MailgunMessagesApi.class);

  @Override
  public void accept(PubSubMessage message, Context context) {
    try {
      String data = message.getData() != null ? new String(Base64.getDecoder().decode(message.getData())) : "No message";
      logger.info("Received message: " + data);
      if (data != null) {
        Gson gson = new Gson();
        EventArtifact eventArtifact = gson.fromJson(data, EventArtifact.class);

        Verify verify = VerifyDao.getVerify(eventArtifact.getUsername(), eventArtifact.getToken());
        if (verify == null) {
          logger.error("Invalid userid and token");
        }
        logger.info("Sending verification email: {}", eventArtifact);
        Message mail = Message.builder()
            .from(EMAIL_FROM)
            .to(eventArtifact.getUsername())
            .subject("CSYE6225 - Verify mail")
            .html(getHtmlContent(eventArtifact.getToken()))
            .build();
        MessageResponse messageResponse = mailgunMessagesApi.sendMessage(DOMAIN, mail);
        logger.info("Mail reponse: {}", messageResponse);
        VerifyDao.updateGeneratedTime(eventArtifact.getToken());
      }
    } catch (Exception e) {
      logger.error("Exception: ", e);
    }
  }

  private String getHtmlContent(String token) {
    return "<html>\n"
        + "<body>\n"
        + "\t<h1>CSYE6225 - Verify mail</h1>\n"
        + "\t<p style=\"color:blue; font-size:30px;\">Click below link to verify your account</p>\n"
        + "\t<p style=\"font-size:30px;\">"
        + "http://"
        + DOMAIN
        + ":8080/v1/user/verify?token="
        + token
        + "</p>\n"
        + "</body>\n"
        + "</html>";
  }
}
