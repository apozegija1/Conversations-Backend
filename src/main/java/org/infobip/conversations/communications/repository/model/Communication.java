package org.infobip.conversations.communications.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.infobip.conversations.common.Constant;
import org.infobip.conversations.communicationreviews.repository.model.CommunicationReview;
import org.infobip.conversations.communicationtypes.repository.model.CommunicationType;
import org.infobip.conversations.users.repository.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Cacheable(false)
@Table(name = "communications")
public class Communication {

   @Id
   @Column(name = "id")
   @JsonProperty("id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JsonProperty("agent")
   @JoinColumn(name = "agent_id")
   private User agent;

   @ManyToOne
   @JsonProperty("customer")
   @JoinColumn(name = "customer_id")
   private User customer;

   @ManyToOne
   @JsonProperty("type")
   @JoinColumn(name = "type_id")
   private CommunicationType type;

   @Column(name = "start_time")
   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Constant.TIMESTAMP_FORMAT, timezone="Europe/Berlin")
   @NotNull
   private Timestamp startTime;

   @Column(name = "end_time")
   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=Constant.TIMESTAMP_FORMAT, timezone="Europe/Berlin")
   @NotNull
   private Timestamp endTime;

   @Column(name = "text", length = 400)
   @NotNull
   @Size(min = 4, max = 400)
   private String text;

   public Communication() {}

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public User getAgent() {
      return agent;
   }

   public void setAgent(User agent) {
      this.agent = agent;
   }

   public User getCustomer() {
      return customer;
   }

   public void setCustomer(User customer) {
      this.customer = customer;
   }

   public CommunicationType getType() {
      return type;
   }

   public void setType(CommunicationType type) {
      this.type = type;
   }

   public Timestamp getStartTime() {
      return startTime;
   }

   public void setStartTime(Timestamp start_time) {
      this.startTime = start_time;
   }

   public Timestamp getEndTime() {
      return endTime;
   }

   public void setEndTime(Timestamp end_time) {
      this.endTime = end_time;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
