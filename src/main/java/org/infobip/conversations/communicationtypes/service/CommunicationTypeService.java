package org.infobip.conversations.communicationtypes.service;

import org.infobip.conversations.communicationtypes.repository.model.CommunicationTypeRepository;
import org.infobip.conversations.companies.repository.model.CompanyRepository;
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
