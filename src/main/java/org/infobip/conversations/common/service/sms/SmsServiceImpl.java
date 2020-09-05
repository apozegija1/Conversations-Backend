package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.service.http.RequestClientService;
import org.infobip.conversations.common.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URL;

@Component
public class SmsServiceImpl implements SmsService {
   @Value("${sms.base_url}")
   private String baseUrl;

   private final String path = "/sms/2/text/advanced";

   private final RequestClientService requestClientService;

   public SmsServiceImpl(RequestClientService requestClientService) {
      this.requestClientService = requestClientService;
   }

   @Override
   public Response sendSms(String fromCompany, String toAddress,
                           String subject, String body) {
      if (toAddress == null) {
         return new Response(ResultCode.ERROR, "Invalid to message.");
      } else {
         try {
            URL url = new URL(baseUrl + path);
            this.requestClientService.post(SmsUtils.getSmsJson(fromCompany, toAddress,
               subject, body), url);
         } catch (Exception ex) {

         }

         return new Response(ResultCode.SUCCESS, "OK");
      }
   }
}
