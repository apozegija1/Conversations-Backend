package org.infobip.conversations.common.model;

public class Message {
   public Message(String id, String from, String to, String subject, String body) {
      this.id = id;
      this.from = from;
      this.to = to;
      this.subject = subject;
      this.body = body;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String getTo() {
      return to;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getBody() {
      return body;
   }

   public void setBody(String body) {
      this.body = body;
   }

   private String id;
   private String from;
   private String to;
   private String subject;
   private String body;
}
