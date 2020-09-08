package org.infobip.conversations.common.service;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.model.MessageType;

public interface MessageService {
   /**
    * Send simple email
    *
    * @param to
    * @param subject
    * @param body
    */
   Response sendMessage(MessageType type, String from, String to, String subject, String body);
}
