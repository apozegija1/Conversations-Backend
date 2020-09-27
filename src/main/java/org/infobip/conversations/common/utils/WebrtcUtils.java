package org.infobip.conversations.common.utils;

import org.infobip.conversations.common.model.WebRtcTokenResponse;
import org.json.JSONObject;

public class WebrtcUtils {
   public static JSONObject getTokenRequestJson(String identity, String appId, String displayName) {
      JSONObject data = new JSONObject();
      data.put("identity", identity);

      data.put("displayName", displayName);
      return data;
   }

   public static WebRtcTokenResponse getTokenFromJson(JSONObject data) {
      if (data == null) {
         return null;
      }

      String token = data.getString("token");
      String expirationTime = data.getString("expirationTime");
      return new WebRtcTokenResponse(token, expirationTime);
   }
}
