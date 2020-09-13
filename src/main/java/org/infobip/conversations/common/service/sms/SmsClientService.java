package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.model.ExternalAuthDto;
import org.infobip.conversations.common.service.http.RequestClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

   @Value("${sms.api_auth_type}")
   private String authType;

   @Value("${sms.api_path}")
   private String path;

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public JSONObject post(JSONObject jsonObject) {
      JSONObject jsonReturn = null;
      try {
         RequestClient client = this.getRequestClient();
         jsonReturn = client.post(jsonObject, this.getUrl());
      } catch (IOException e) {
         logger.error(e.getLocalizedMessage(), e);
      }

      return jsonReturn;
   }

   public void delete(URL url) {
      try {
         RequestClient client = this.getRequestClient();
         client.delete(this.getUrl());
      } catch (IOException e) {
         logger.error(e.getLocalizedMessage(), e);
      }
   }

   private URL getUrl() throws MalformedURLException {
      return new URL(this.baseUrl + this.path);
   }

   private RequestClient getRequestClient() {
      return new RequestClient(new ExternalAuthDto(this.authType, this.apiKey));
   }
}
