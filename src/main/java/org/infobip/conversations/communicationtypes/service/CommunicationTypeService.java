package org.infobip.conversations.communicationtypes.service;

import org.infobip.conversations.communicationtypes.repository.CommunicationTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommunicationTypeService {
   private final CommunicationTypeRepository communicationTypeRepository;

   public CommunicationTypeService(CommunicationTypeRepository communicationTypeRepository) {
      this.communicationTypeRepository = communicationTypeRepository;
   }

}
