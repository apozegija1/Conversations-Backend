package org.infobip.conversations.communications.repository.model;

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
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "agent_id")
   private User agent;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "customer_id")
   private User customer;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "type_id")
   private CommunicationType type;

   @Column(name = "start_time")
   @NotNull
   private Timestamp start_time;

   @Column(name = "end_time")
   @NotNull
   private Timestamp end_time;

   @Column(name = "text", length = 400)
   @NotNull
   @Size(min = 4, max = 400)
   private String text;

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

   public Timestamp getStart_time() {
      return start_time;
   }

   public void setStart_time(Timestamp start_time) {
      this.start_time = start_time;
   }

   public Timestamp getEnd_time() {
      return end_time;
   }

   public void setEnd_time(Timestamp end_time) {
      this.end_time = end_time;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
