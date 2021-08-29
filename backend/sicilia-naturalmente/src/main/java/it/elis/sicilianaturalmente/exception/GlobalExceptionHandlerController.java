package it.elis.sicilianaturalmente.exception;


import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse()
                .setTimestamp(LocalDateTime.now().toString())
                .setError(ex.getHttpStatus().getReasonPhrase())
                .setStatus(ex.getHttpStatus().value())
                .setMessage(ex.getMessage())
                .setPath(request.getRequestURL().toString());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request) throws IOException {
        HttpStatus responseStatus = HttpStatus.FORBIDDEN;
        ErrorResponse errorResponse = new ErrorResponse()
                .setTimestamp(LocalDateTime.now().toString())
                .setError(responseStatus.getReasonPhrase())
                .setStatus(responseStatus.value())
                .setMessage("Access denied")
                .setPath(request.getRequestURL().toString());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request) throws IOException {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse()
                .setTimestamp(LocalDateTime.now().toString())
                .setError(responseStatus.getReasonPhrase())
                .setStatus(responseStatus.value())
                .setMessage("Something went wrong")
                .setPath(request.getRequestURL().toString());
        return new ResponseEntity<>(errorResponse, responseStatus);
    }

}

