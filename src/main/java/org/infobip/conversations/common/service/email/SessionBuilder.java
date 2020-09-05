package org.infobip.conversations.common.service.email;

import javax.mail.Session;
import java.util.Properties;

public class SessionBuilder {
   private Properties props;

   public SessionBuilder() {
      props = System.getProperties();
   }

   public SessionBuilder addPort(int port) {
      props.put("mail.smtp.port", port);
      return this;
   }

   public SessionBuilder addHost(String host) {
      props.put("mail.smtps.host", host);
      return this;
   }

   public SessionBuilder addProtocol(String protocol) {
      props.put("mail.transport.protocol", protocol);
      return this;
   }

   public SessionBuilder addDefaultSmtpsProtocol() {
      props.put("mail.transport.protocol", "smtps");
      return this;
   }

   public SessionBuilder addAuthEnabled(String enabled) {
      props.put("mail.smtp.auth", enabled);
      return this;
   }

   public SessionBuilder addDefaultAuthEnabled() {
      props.put("mail.smtp.auth", "true");
      return this;
   }

   public SessionBuilder addTtlsEnable(String enabled) {
      props.put("mail.smtp.starttls.enable", enabled);
      return this;
   }

   public SessionBuilder addDefaultTtlsEnable() {
      props.put("mail.smtp.starttls.enable",  "true");
      return this;
   }

   public SessionBuilder addTtlsRequired(String enabled) {
      props.put("mail.smtp.starttls.required", enabled);
      return this;
   }

   public SessionBuilder addDefaultTtlsRequired() {
      props.put("mail.smtp.starttls.required",  "true");
      return this;
   }

   public Session build() {
      return Session.getDefaultInstance(props);
   }
}
