package org.infobip.conversations.common.service.sms;

import org.infobip.conversations.common.Response;

public interface SmsService {
   Response sendSms(String from, String to, String subject, String body);
}
