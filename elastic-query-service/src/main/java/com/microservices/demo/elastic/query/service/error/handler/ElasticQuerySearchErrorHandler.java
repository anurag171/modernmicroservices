package com.microservices.demo.elastic.query.service.error.handler;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ElasticQuerySearchErrorHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handle(AccessDeniedException ex){
    log.info("Access denied exception [{}]",ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to access this resource");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handle(IllegalArgumentException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegalargument exception "+ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Runtime exception "+ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handle(Exception ex){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Generic exception "+ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,String>> handle(MethodArgumentNotValidException ex){
    Map<String,String> errorMap = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(objectError ->errorMap.put(((FieldError)objectError).getField(),objectError.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
  }

}
