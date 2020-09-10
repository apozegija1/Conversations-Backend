package org.infobip.conversations.common.model;

public class ExternalAuthDto {
   public ExternalAuthDto(String type, String token) {
      this.type = type;
      this.token = token;
   }

   public String type;
   public String token;
}
