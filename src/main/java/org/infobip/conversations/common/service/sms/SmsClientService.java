package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.model.ExternalAuthDto;
import org.infobip.conversations.common.service.http.RequestClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URL;

@Service
public class SmsClientService {
   @Value("${sms.base_url}")
   private String baseUrl;

   @Value("${sms.api_key}")
   private String apiKey;

   private String authType = "App";

   private final String path = "/sms/2/text/advanced";

   public JSONObject post(JSONObject jsonObject) {
      JSONObject jsonReturn = null;
      try {
         RequestClient client = this.getRequestClient();
         jsonReturn = client.post(jsonObject, this.getUrl());
      } catch (IOException e) {

      }

      return jsonReturn;
   }

   public void delete(URL url) {
      JSONObject jsonReturn = null;
      try {
         RequestClient client = this.getRequestClient();
         client.delete(this.getUrl());
      } catch (IOException e) {

      }
      /*try {
         HttpURLConnection con = new HttpRequestBuilder(url)
            .addMethod("DELETE")
            .build();

         con.setDoOutput(true);
         con.connect();

         String json = this.readJson(con.getInputStream());
         if (json == null) return;
      } catch (ProtocolException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }*/
   }

   private URL getUrl() throws MalformedURLException {
      return new URL(this.baseUrl + this.path);
   }

   private RequestClient getRequestClient() {
      return new RequestClient(new ExternalAuthDto(this.authType, this.apiKey));
   }
}
