package org.infobip.conversations.communications.service;

import org.infobip.conversations.communications.repository.model.CommunicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommunicationService {
   private final CommunicationRepository communicationRepository;

   public CommunicationService(CommunicationRepository communicationRepository) {
      this.communicationRepository = communicationRepository;
   }

}
