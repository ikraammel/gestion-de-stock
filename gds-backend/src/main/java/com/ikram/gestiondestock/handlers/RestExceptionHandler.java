package com.ikram.gestiondestock.handlers;

import com.ikram.gestiondestock.exception.EntityNotFoundException;
import com.ikram.gestiondestock.exception.InvalidEntityException;
import com.ikram.gestiondestock.exception.InvalidOperationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(EntityNotFoundException exception, WebRequest webRequest){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCodes())
                .httpCode(notFound.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDto,notFound);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidEntityException exception,WebRequest webRequest){
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCodes())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .errors(exception.getErrors())
                .build();
        return new ResponseEntity<>(errorDto,badRequest);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidOperationException exception,WebRequest webRequest){
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCodes())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDto,badRequest);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String message = "Une entreprise avec cet email existe déjà";

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "errors", List.of(message)
                ));
    }
}
