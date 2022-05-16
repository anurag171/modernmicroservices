package com.microservices.demo.elastic.query.web.client.common.api.error.handler;


import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ElasticQueryWebClientErrorHandler {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";


    @ExceptionHandler(AccessDeniedException.class)
    public String handle(AccessDeniedException e, Model model) {
        log.error("Access denied exception!");
        model.addAttribute(ERROR, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        model.addAttribute("error_description, You are not authorized to access this resource!");
        return ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(IllegalArgumentException e, Model model) {
        log.error("Illegal argument exception!", e);
        model.addAttribute(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, "Illegal argument exception!" + e.getMessage());
        return ERROR;
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception e, Model model) {
        log.error("Internal server error!", e);
        model.addAttribute(ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, "A server error occurred!");
        return ERROR;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handle(RuntimeException e, Model model) {
        log.error("Service runtime exception!", e);
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute(ERROR, "Could not get response! " + e.getMessage());
        model.addAttribute(ERROR_DESCRIPTION, "Service runtime exception! " + e.getMessage());
        return "home";
    }

    @ExceptionHandler({BindException.class})
    public String handle(BindException e, Model model) {
        log.error("Method argument validation exception!", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, errors);
        return "home";
    }

}
