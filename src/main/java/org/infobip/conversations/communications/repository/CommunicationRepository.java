package org.infobip.conversations.communications.repository;

import org.infobip.conversations.communications.repository.model.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

   @Query("SELECT c " +
      "FROM  CommunicationType t, Communication c, User ua, User uc " +
      "WHERE t.id = c.type.id AND uc.id = c.customer.id AND ua.id = c.agent.id " +
      "AND (c.agent.id = ?1 OR c.customer.id = ?2)")
      //if agent is null, we will get all communications for a specific user,
      //and if customer is null, we will get all communications for agent
    List<Communication> findAllCommunicationsForUser(Long agentId, Long customerId);


   @Query("SELECT cm " +
      "FROM Communication cm, User ua, User uc, Company cp " +
      "WHERE cm.agent.id = ua.id AND cm.customer.id = uc.id AND ua.company.id = cp.id " +
      "AND cp.id = ?1")
   List<Communication> findAllCommunicationsForCompany(Long companyId);

}

