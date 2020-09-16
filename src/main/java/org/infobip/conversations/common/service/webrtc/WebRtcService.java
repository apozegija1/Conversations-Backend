package org.infobip.conversations.common.service.webrtc;

import org.infobip.conversations.common.model.WebRtcTokenResponse;
import org.infobip.conversations.common.service.http.InfobipRequestClientService;
import org.infobip.conversations.common.utils.WebrtcUtils;
import org.infobip.conversations.users.repository.model.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebRtcService {
   @Value("${infobip.webrtc.api_path}")
   private String path;

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   private final InfobipRequestClientService infobipRequestClientService;

   public WebRtcService(InfobipRequestClientService infobipRequestClientService) {
      this.infobipRequestClientService = infobipRequestClientService;
   }

   private JSONObject post(JSONObject jsonObject) {
      return this.infobipRequestClientService.post(jsonObject, null, this.path);
   }

   public WebRtcTokenResponse obtainToken(User user) {
      String name = user.getFullName();
      JSONObject obj = WebrtcUtils.getTokenRequestJson(user.getUsername(), this.path, name);
      JSONObject response = this.post(obj);
      return WebrtcUtils.getTokenFromJson(response);
   }
}
