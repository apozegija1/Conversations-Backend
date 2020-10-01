package org.infobip.conversations.common.model;

public class WebRtcTokenResponse {
   public String token;
   public String expirationTime;

   public WebRtcTokenResponse(String token, String expirationTime) {
      this.token = token;
      this.expirationTime = expirationTime;
   }
}
