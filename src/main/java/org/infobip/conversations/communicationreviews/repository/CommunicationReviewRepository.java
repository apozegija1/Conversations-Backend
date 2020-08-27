package org.infobip.conversations.communicationreviews.repository;

import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationReviewRepository extends JpaRepository<CommunicationReview, Long> {

   @Query("SELECT cr " +
      "FROM  CommunicationReview cr, Communication c, User ua, User uc " +
      "WHERE cr.communication.id = c.id AND uc.id = c.customer.id AND ua.id = c.agent.id " +
      "AND (c.agent.id = ?1 OR c.customer.id = ?2)")
      //if agent is null, we will get all communications for a specific user,
      //and if customer is null, we will get all communications for agent
   List<CommunicationReview> findAllReviewsForUser(Long agentId, Long customerId);


   @Query("SELECT cr " +
      "FROM  CommunicationReview cr, Communication c, User ua, User uc " +
      "WHERE cr.communication.id = c.id AND uc.id = c.customer.id AND ua.id = c.agent.id " +
      "AND (c.agent.username = ?1 OR c.customer.username = ?2)")

   List<CommunicationReview> findAllReviewsForUserByUsername(String agentUsername, String customerUsername);


   @Query(value = "SELECT AVG(cr.rating) " +
      "FROM communicationreviews cr, communications cm, users ua, users uc, companies cp, communicationtypes ct " +
      "WHERE cr.communication_id = cm.id AND cm.agent_id = ua.id AND cm.customer_id = ua.id " +
      "AND ua.company_id = cp.id AND cm.type_id = ct.id " +
      "AND (cp.id = ?1 OR ct.id = ?2) " +
      "GROUP BY ct.type", nativeQuery = true)

   Float findAverageRatingForCompany(Long companyId, Long typeId);
}
