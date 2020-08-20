package org.infobip.conversations.communicationtypes.repository.model;

import org.infobip.conversations.communications.repository.model.Communication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Long> {
}
