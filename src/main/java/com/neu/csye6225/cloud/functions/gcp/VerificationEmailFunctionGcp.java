package com.neu.csye6225.cloud.functions.gcp;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.neu.csye6225.cloud.functions.core.VerificationEmailProcessor;
import com.neu.csye6225.cloud.functions.entity.PubSubMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificationEmailFunctionGcp implements BackgroundFunction<PubSubMessage> {

  private static final Logger logger = LoggerFactory.getLogger(VerificationEmailFunctionGcp.class);

  private static final VerificationEmailProcessor processor = new VerificationEmailProcessor();

  @Override
  public void accept(PubSubMessage message, Context context) {
    try {
      String dataB64 = message == null ? null : message.getData();
      String json = (dataB64 == null) ? null : new String(Base64.getDecoder().decode(dataB64), StandardCharsets.UTF_8);

      logger.info("Received PubSub payload: {}", json);
      processor.processJson(json);

    } catch (Exception e) {
      logger.error("GCP handler failed", e);
    }
  }
}
