package com.neu.csye6225.cloud.functions.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.neu.csye6225.cloud.functions.core.VerificationEmailProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerificationEmailFunctionAws implements RequestHandler<SNSEvent, Void> {

  private static final Logger logger = LoggerFactory.getLogger(VerificationEmailFunctionAws.class);
  private static final VerificationEmailProcessor processor = new VerificationEmailProcessor();

  @Override
  public Void handleRequest(SNSEvent event, Context context) {
    try {
      if (event == null || event.getRecords() == null || event.getRecords().isEmpty()) {
        logger.warn("Empty SNS event");
        return null;
      }

      String json = event.getRecords().get(0).getSNS().getMessage();
      logger.info("Received SNS payload: {}", json);

      processor.processJson(json);
      return null;

    } catch (Exception e) {
      logger.error("AWS SNS handler failed", e);
      return null;
    }
  }
}
