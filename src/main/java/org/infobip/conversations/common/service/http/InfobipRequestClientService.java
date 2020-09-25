package org.infobip.conversations.common.service.http;

import org.infobip.conversations.common.model.ExternalAuthDto;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class InfobipRequestClientService {
   @Value("${infobip.base_url}")
   private String baseUrl;

   @Value("${infobip.api_key}")
   private String apiKey;

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public InfobipRequestClientService() {

   }

   public JSONObject post(JSONObject jsonObject, String authType, String path) {
      JSONObject jsonReturn = null;
      try {
         RequestClient client = this.getRequestClient(authType);
         jsonReturn = client.post(jsonObject, this.getUrl(path));
      } catch (MalformedURLException e) {
         logger.error(e.getLocalizedMessage(), e);
      }

      return jsonReturn;
   }

   public String getApiKey() {
      return this.apiKey;
   }

   private URL getUrl(String path) throws MalformedURLException {
      return new URL(this.baseUrl + path);
   }

   private RequestClient getRequestClient(String authType) {
      return new RequestClient(new ExternalAuthDto(authType, this.apiKey));
   }
}
