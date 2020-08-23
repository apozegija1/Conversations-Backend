package org.infobip.conversations.communicationreviews.repository;

import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationReviewRepository extends JpaRepository<CommunicationReview, Long> {
}
