package org.infobip.conversations.communicationreviews.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.infobip.conversations.communications.repository.model.Communication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Cacheable(false)
@Table(name = "communicationreviews")
public class CommunicationReview {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "date")
   @Temporal(TemporalType.DATE)
   @NotNull
   private Date date;

   @JsonProperty("communication")
   @OneToOne
   @JoinColumn(name = "communication_id", referencedColumnName = "id")
   private Communication communication;

   @Column(name = "rating")
   @NotNull
   private int rating;

   @Column(name = "description", length = 400)
   @Size(min = 4, max = 400)
   private String description;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Communication getCommunication() {
      return communication;
   }

   public void setCommunication(Communication communication) {
      this.communication = communication;
   }

   public int getRating() {
      return rating;
   }

   public void setRating(int rating) {
      this.rating = rating;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}
