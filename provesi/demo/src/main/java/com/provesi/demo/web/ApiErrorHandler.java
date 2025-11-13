package com.provesi.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice

public class ApiErrorHandler {
@ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Map<String,String> denied(AccessDeniedException ex){
    return Map.of("error","FORBIDDEN","msg", ex.getMessage()==null?"Acceso denegado":"Acceso denegado: "+ex.getMessage());
  }
}
