package org.infobip.conversations.common.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.mail.MailException;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;


@Component
public class EmailSenderServiceImpl implements  EmailSenderService {
   private final Logger log = LoggerFactory.getLogger(this.getClass());

   private final Environment env;

   @Value("${email.from.address}")
   private String FROM;

   // Supply your SMTP credentials below.
   private static String SMTP_USERNAME = "";  // Replace with your SMTP username.
   private static String SMTP_PASSWORD = "";  // Replace with your SMTP password.
   private static String HOST = "";
   private static int MAX_RETRIES = 5;

   // The port you will connect to on the Mailgun SMTP endpoint. We are choosing port 587 because we will use
   private static final int PORT = 587;

   public EmailSenderServiceImpl(Environment env) {
      this.env = env;
   }

   @Async
   public void sendSimpleEmail(String toEmail, String subject, String body) {
      try {
         this.setPropertiesVariables();
         log.info("Start send Email to " + toEmail + ", HOST=" + HOST + ", SMTP_USERNAME=" + SMTP_USERNAME);

         // Create a Session object to represent a mail session with the specified properties.
         Session session = getSession();
         // Create a message with the specified information.
         MimeMessage msg  = new MessageBuilder(session)
            .addHeaders(subject, FROM, toEmail)
            .addBody(body)
            // .addAttachmentFiles(emailFiles)
            .build();

         this.connectAndSendEmail(session, msg, toEmail);
      } catch (Exception ex){
         log.error("Error message: " + ex.getMessage() + " Cannot send to email " + toEmail);
      }
   }

   public static long getSleepDuration(int currentTry, long minSleepMillis, long maxSleepMillis) {
      currentTry = Math.max(0, currentTry);
      long currentSleepMillis = (long) (minSleepMillis*Math.pow(2, currentTry));
      return Math.min(currentSleepMillis, maxSleepMillis);
   }

   private void connectAndSendEmail(Session session, MimeMessage msg, String toEmail) {
      try (Transport transport = session.getTransport()) {
         // Connect to Amazon SES using the SMTP username and password you specified above.
         transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
         // Send the message.
         this.trySendEmail(transport, msg, toEmail);
      } catch (Exception ex) {
         log.error("Error message: " + ex.getMessage() + " Cannot send to email " + toEmail);
      }
   }

   private void trySendEmail(Transport transport, MimeMessage msg, String toEmail) {
      int currentTry = 0;
      int maxRetries = MAX_RETRIES;
      while (maxRetries-- > 0) {
         try {
            currentTry++;
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Mailgun has sent email to " + toEmail);
            break;
         } catch (MailException e) {
            log.info("Maximum send rate exceeded when sending email to " + toEmail + ". "
               + (maxRetries > 1 ? "Will retry after backoff." : "Will not retry after backoff."));
            long backoffDuration = getSleepDuration(currentTry, 10, 5000);
            try {
               Thread.sleep(backoffDuration);
            } catch (InterruptedException e1) {
               log.info("Thread sleep interrupted while sending mail, tried to retry");
            }
         } catch (Exception e) {
            log.error("Unable to send email to: " + toEmail + ". " + e.toString());
            break;
         }
      }
   }

   private void setPropertiesVariables() {
      HOST = env.getProperty("email.smtp.host");
      SMTP_USERNAME = env.getProperty("email.smtp.username");
      SMTP_PASSWORD = env.getProperty("email.smtp.password");
      String maxRetriesEnv = env.getProperty("email.max_retries");
      if (maxRetriesEnv != null) {
         MAX_RETRIES = Integer.parseInt(maxRetriesEnv);
      }
   }

   private Session getSession() {
      return new SessionBuilder()
         .addHost(HOST)
         .addPort(PORT)
         .addDefaultSmtpsProtocol()
         .addDefaultAuthEnabled()
         .addDefaultTtlsEnable()
         .addDefaultTtlsRequired()
         .build();
   }
}
