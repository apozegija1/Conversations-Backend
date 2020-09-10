package org.infobip.conversations.common.service;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.model.Message;
import org.infobip.conversations.common.model.MessageType;

public interface MessageService {
   /**
    * Send simple email
    *
    */
   Response sendMessage(MessageType type, Message message);
}
