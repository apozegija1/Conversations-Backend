package org.infobip.conversations.common.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class SmsUtils {
   public static JSONObject getSmsJson(String id, String from, String to,
                                String body) {
      JSONObject sms = new JSONObject();

      // Creating messages object to send in json
      JSONObject messages = new JSONObject();
      messages.put("text", body);
      messages.put("from", from);
      JSONArray tos = new JSONArray();
      tos.put(to);
      // Array of destination addresses as string
      messages.put("to", tos);
      JSONArray destinations = new JSONArray();
      JSONObject destination = new JSONObject();
      destination.put("to", to);
      destination.put("messageId", id);
      destinations.put(destination);
      // Object of destination addresses with message id and address to destination
      messages.put("destinations", destinations);

      sms.put("messages", messages);
      return sms;
   }
}
