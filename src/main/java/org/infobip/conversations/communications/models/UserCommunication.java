package org.infobip.conversations.communications.models;

import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.users.repository.model.User;

import java.util.List;

public class UserCommunication {
   public User user;
   public List<Communication> communications;

   public UserCommunication(User user, List<Communication> communications) {
      this.user = user;
      this.communications = communications;
   }
}
