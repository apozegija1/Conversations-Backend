package org.infobip.conversations.communicationtypes.repository;

import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Long> {
}
