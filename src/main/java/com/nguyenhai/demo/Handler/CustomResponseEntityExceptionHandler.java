package com.nguyenhai.demo.Handler;

import com.nguyenhai.demo.Response.EntityExceptionResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handlerGlobalException(Throwable e) {
        String messages = e.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Date dateCreate = new Date(System.currentTimeMillis());

        return ResponseEntity
                .status(status).contentType(MediaType.APPLICATION_JSON)
                .body(new EntityExceptionResponse(messages, status, dateCreate));
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {
        ObjectError error = ex.getBindingResult().getAllErrors().get(0);
        String firstMessage = new DefaultMessageSourceResolvable(error).getDefaultMessage();
        Date dateCreate = new Date(System.currentTimeMillis());

        return ResponseEntity
                .status(status).contentType(MediaType.APPLICATION_JSON)
                .body(new EntityExceptionResponse(firstMessage, status, dateCreate));
    }
}
