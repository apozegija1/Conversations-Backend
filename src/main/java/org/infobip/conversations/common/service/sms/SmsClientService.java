package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.service.http.InfobipRequestClientService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsClientService {
   @Value("${infobip.sms.api_auth_type}")
   private String authType;

   @Value("${infobip.sms.api_path}")
   private String path;

   private final InfobipRequestClientService infobipRequestClientService;

   public SmsClientService(InfobipRequestClientService infobipRequestClientService) {
      this.infobipRequestClientService = infobipRequestClientService;
   }

   public JSONObject post(JSONObject jsonObject) {
      return this.infobipRequestClientService.post(jsonObject, this.authType, this.path);
   }
}
