package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.utils.SmsUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class SmsServiceImpl implements SmsService {

   private final SmsClientService requestClient;

   public SmsServiceImpl(SmsClientService requestClient) {
      this.requestClient = requestClient;
   }

   @Override
   public Response sendSms(String id, String fromCompany, String toAddress,
                           String subject, String body) {
      JSONObject data;
      if (toAddress == null) {
         return new Response(ResultCode.ERROR, "Invalid to message.");
      } else {
         try {
            data = this.requestClient.post(SmsUtils.getSmsJson(id, fromCompany,
               toAddress, body));
         } catch (Exception ex) {
            return new Response(ResultCode.ERROR, ex.getLocalizedMessage());
         }

         return new Response(ResultCode.SUCCESS, "OK").setResult(data);
      }
   }
}
