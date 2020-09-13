package org.infobip.conversations.common.service.http;

import org.infobip.conversations.common.model.ExternalAuthDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestClient {
   private ExternalAuthDto externalAuthDto;

   public RequestClient(ExternalAuthDto authDto) {
      this.externalAuthDto = authDto;
   }

   private String readJson(InputStream in) {
      try {
         BufferedReader entry = new BufferedReader(new InputStreamReader(in,
            StandardCharsets.UTF_8));
         StringBuilder json = new StringBuilder();
         String line = "";
         while ((line = entry.readLine()) != null) {
            json.append(line);
         }

         entry.close();
         if (json.length() == 0) return null;
         return (json.toString());
      } catch (IOException e) {
         e.printStackTrace();

      }
      return null;
   }

   private String getJsonDataFromUrl(URL url) {
      try {
         HttpRequestBuilder conBuilder = new HttpRequestBuilder(url);

         conBuilder = this.addAuth(conBuilder);
         HttpURLConnection con = conBuilder.build();

         InputStream in = con.getInputStream();
         return this.readJson(in);
      } catch (IOException e) {
         e.printStackTrace();

      }
      return null;
   }

   public JSONArray getJsonArray(URL url) {
      JSONArray jsonArray;
      String json = this.getJsonDataFromUrl(url);
      if (json == null) return null;
      jsonArray = new JSONArray(json);
      return jsonArray;
   }

   public JSONObject getJsonObject(URL url) {
      JSONObject jsonArray;
      String json = this.getJsonDataFromUrl(url);
      if (json == null) return null;
      jsonArray = new JSONObject(json);
      return jsonArray;
   }

   public JSONObject post(JSONObject jsonObject, URL url) {
      JSONObject jsonObjectReturn = null;
      try {

         byte[] data = jsonObject.toString().getBytes();
         HttpRequestBuilder conBuilder = new HttpRequestBuilder(url)
            .addMethod("POST")
            .addData(data);

         conBuilder = this.addAuth(conBuilder);
         HttpURLConnection con = conBuilder.build();

         String json = this.readJson(con.getInputStream());
         if (json == null) return null;
         jsonObjectReturn = new JSONObject(json);
      } catch (IOException e) {
         System.out.println(e.getLocalizedMessage());
      }
      return jsonObjectReturn;
   }

   private void update(JSONObject jo, URL url) {
      try {
         byte[] data = jo.toString().getBytes();
         HttpRequestBuilder conBuilder = new HttpRequestBuilder(url)
            .addMethod("PUT")
            .addData(data);

         conBuilder = this.addAuth(conBuilder);
         HttpURLConnection con = conBuilder.build();
         // con.connect();

         String json = this.readJson(con.getInputStream());
         if (json == null) return;
      } catch (IOException e) {

      }
   }

   public void delete(URL url) {
      try {
         HttpRequestBuilder conBuilder = new HttpRequestBuilder(url)
            .addMethod("DELETE");

         conBuilder = this.addAuth(conBuilder);
         HttpURLConnection con = conBuilder.build();

         con.setDoOutput(true);
         con.connect();

         String json = this.readJson(con.getInputStream());
         if (json == null) return;
      } catch (ProtocolException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private HttpRequestBuilder addAuth(HttpRequestBuilder conBuilder) {
      if (this.externalAuthDto != null) {
         conBuilder = conBuilder
            .addAuth(this.externalAuthDto);
      }
      return conBuilder;
   }
}
