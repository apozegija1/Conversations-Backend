package org.infobip.conversations.common.service.http;

import org.infobip.conversations.common.model.ExternalAuthDto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestBuilder {
   private String method = "GET";
   private final URL url;
   private String contentType = "application/json";
   private String token = null;
   private String authType = "Bearer";
   private byte[] data = null;

   public HttpRequestBuilder(URL url) {
      this.url = url;
   }

   public HttpRequestBuilder addMethod(String method) {
      this.method = method;
      return this;
   }

   public HttpRequestBuilder addAuth(ExternalAuthDto authDto) {
      this.token = authDto.token;
      if (authDto.type != null) {
         this.authType = authDto.type;
      }

      return this;
   }

   public HttpRequestBuilder addData(byte[] data) {
      this.data = data;
      return this;
   }

   public HttpRequestBuilder addContentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   protected HttpURLConnection getBaseHttpConnection() throws IOException {
      HttpURLConnection con;
      con = (HttpURLConnection) this.url.openConnection();
      con.setRequestProperty("Access-Control-Allow-Origin", "*");
      con.setRequestMethod(this.method);
      con.setRequestProperty("Content-Type", this.contentType);

      return con;
   }


   public HttpURLConnection build() throws IOException {
      HttpURLConnection con = this.getBaseHttpConnection();
      if (this.token != null) {
         String basicAuth = this.authType + " " + this.token;
         con.setRequestProperty ("Authorization", basicAuth);
      }

      if(this.data != null) {
         con.setDoOutput(true);
         DataOutputStream out = new DataOutputStream(con.getOutputStream());
         out.write(data);
         out.flush();
         out.close();
      }

      return con;
   }
}
