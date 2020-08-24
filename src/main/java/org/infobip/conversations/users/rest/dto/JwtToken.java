package org.infobip.conversations.users.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken {

   private String idToken;

   public JwtToken(String idToken) {
      this.idToken = idToken;
   }

   @JsonProperty("token")
   String getIdToken() {
      return idToken;
   }

   void setIdToken(String idToken) {
      this.idToken = idToken;
   }
}
