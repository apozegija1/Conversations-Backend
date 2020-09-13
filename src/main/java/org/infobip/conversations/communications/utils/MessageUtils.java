package org.infobip.conversations.communications.utils;

import org.infobip.conversations.common.model.MessageType;
import org.infobip.conversations.communications.models.AvailableCommunicationType;
import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;

public class MessageUtils {
   public static MessageType getMessageTypeFromCommunicationType(CommunicationType type) {
      if (type.getType().equals(AvailableCommunicationType.Sms.name())) {
         return MessageType.Sms;
      }

      return null;
   }
}
