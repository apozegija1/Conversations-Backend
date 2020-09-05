package org.infobip.conversations.common.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class SmsUtils {
   public static JSONObject getSmsJson(String from, String to,
                                String subject, String body) {
      JSONObject sms = new JSONObject();

      // Creating messages object to send in json
      JSONObject messages = new JSONObject();
      messages.put("text", body);
      messages.put("from", from);
      JSONArray destinations = new JSONArray();
      JSONObject destination = new JSONObject();
      destination.put("to", to);
      destination.put("messageId", to);
      destinations.put(destination);
      messages.put("destinations", destinations);

      sms.put("messages", messages);
      return sms;
   }
}
