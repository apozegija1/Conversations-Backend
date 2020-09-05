package org.infobip.conversations.common.service;

import org.infobip.conversations.common.Response;
import org.infobip.conversations.common.ResultCode;
import org.infobip.conversations.common.model.MessageType;
import org.infobip.conversations.common.service.email.EmailSenderService;
import org.infobip.conversations.common.service.sms.SmsService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
   private final SmsService smsService;
   private final EmailSenderService emailSenderService;

   MessageServiceImpl(SmsService smsService,
                      EmailSenderService emailSenderService) {
      this.smsService = smsService;
      this.emailSenderService = emailSenderService;
   }

   @Override
   public Response sendMessage(MessageType type, String toAddress,
                               String subject, String body) {
      if (toAddress == null) {
         return new Response(ResultCode.ERROR, "Invalid to message.");
      } else {
         if (type == MessageType.Sms) {
            this.smsService.sendSms(toAddress, subject, body);
         } else if(type == MessageType.Email) {
            this.emailSenderService.sendSimpleEmail(toAddress, subject, body);
         }

         return new Response(ResultCode.SUCCESS, "OK");
      }
   }
}
