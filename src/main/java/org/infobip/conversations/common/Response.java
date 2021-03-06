package org.infobip.conversations.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

   private static final long serialVersionUID = 6474479867580063500L;

   public ResultCode status;
   public Object data;
   public String message;

   public Response(ResultCode status, String message) {
      this.status = status;
      this.message = message;
   }

   public Response setResult(Object data) {
      this.data = data;
      return this;
   }
}
