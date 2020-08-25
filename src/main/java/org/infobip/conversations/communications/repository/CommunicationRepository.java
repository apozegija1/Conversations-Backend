package org.infobip.conversations.communications.repository;

import org.infobip.conversations.communications.repository.model.Communication;
import org.infobip.conversations.users.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {

   @Query("SELECT t.type, CONCAT_WS(' ', ua.first_name, ua.last_name) AS agent, CONCAT_WS(' ', uc.first_name, uc.last_name) AS customer, c.start_time, c.end_time, c.text " +
      "FROM  communicationtypes t, communications c, users ua, users uc " +
      "WHERE t.id = c.type_id AND uc.id = c.customer_id AND ua.id = c.agent_id " +
      "AND (c.agent_id = ?1 OR c.customer_id = ?2)")
      //if agent is null, we will get all communications for a specific user,
      //and if customer is null, we will get all communications for agent
    List<Object> findAllCommunicationsForUser(String agent_id, String customer_id);
}
