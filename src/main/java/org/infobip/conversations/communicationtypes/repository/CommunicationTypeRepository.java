package org.infobip.conversations.communicationtypes.repository;

import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.infobip.conversations.users.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Long> {
   Optional<CommunicationType> findOneByType(String type);
}
