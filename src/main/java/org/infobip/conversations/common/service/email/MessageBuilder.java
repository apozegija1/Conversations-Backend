package org.infobip.conversations.common.service.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

public class MessageBuilder {
   private MimeMessage msg;
   private Session session;
   private Multipart multipart;

   private String subject;
   private String sender;
   private String recipient;
   private String body;

   private String bodyTypeHtml = "text/html";
   private String charset = "UTF-8";

   public MessageBuilder(Session session) {
      msg = new MimeMessage(session);
   }

   public MessageBuilder addHeaders(String subject, String sender, String recipient) throws UnsupportedEncodingException, MessagingException {
      this.subject = subject;
      this.sender = sender;
      this.recipient = recipient;
      this.buildHeaders();
      return this;
   }

   private void buildHeaders() throws MessagingException, UnsupportedEncodingException {
      msg.setFrom(new InternetAddress(this.sender, subject, charset));
      msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(this.recipient));
      msg.setSubject(subject, charset);
   }

   public MessageBuilder addBody(String subject) {
      this.body = subject;
      return this;
   }

   public MessageBuilder addCharset(String charset) {
      this.charset = charset;
      return this;
   }

   /*public MessageBuilder addAttachmentFiles(EmailFile[] emailFiles) throws MessagingException {
      if (emailFiles.length > 0) {
         Multipart multipart = new MimeMultipart();
         BodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setText(body);

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         Arrays.stream(emailFiles).forEach(emailFile -> {
            MimeBodyPart att = new MimeBodyPart();
            ByteArrayDataSource bds = new ByteArrayDataSource(emailFile.file, emailFile.fileType);
            bds.setName(emailFile.fileName);
            try {
               att.setDataHandler(new DataHandler(bds));
               att.setFileName(bds.getName());
               multipart.addBodyPart(att);
            } catch (MessagingException e) {
               e.printStackTrace();
            }
         });

         this.multipart = multipart;
      }

      return this;
   }*/

   public MessageBuilder addBodyImageFile(String bodyImage) throws MessagingException {
      multipart = new MimeMultipart("related");
      BodyPart messageBodyPart = new MimeBodyPart();

      messageBodyPart.setContent(body, bodyTypeHtml);
      multipart.addBodyPart(messageBodyPart);
      // for body image
      messageBodyPart = new MimeBodyPart();
      DataSource fds = new FileDataSource(bodyImage);
      messageBodyPart.setDataHandler(new DataHandler(fds));
      messageBodyPart.addHeader("Content-ID", "<bodyImage>");
      multipart.addBodyPart(messageBodyPart);
      return this;
   }

   public MimeMessage build() throws MessagingException {
      if (multipart != null) {
         msg.setContent(multipart);
      } else {
         msg.setContent(body, bodyTypeHtml + "; charset=" + charset);
      }
      return msg;
   }
}
