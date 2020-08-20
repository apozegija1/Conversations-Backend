package org.infobip.conversations.communicationreviews.service;

import org.infobip.conversations.communicationreviews.model.CommunicationReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommunicationReviewService {
   private final CommunicationReviewRepository communicationReviewRepository;

   public CommunicationReviewService(CommunicationReviewRepository communicationReviewRepository) {
      this.communicationReviewRepository = communicationReviewRepository;
   }

}
