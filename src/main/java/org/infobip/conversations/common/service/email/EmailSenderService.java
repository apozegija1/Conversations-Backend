package org.infobip.conversations.common.service.email;

public interface EmailSenderService {
   void sendSimpleEmail(String toEmail, String subject, String body);
}
