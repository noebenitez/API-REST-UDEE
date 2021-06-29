package com.utn.udee.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntity extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        List<String> errors = new ArrayList<>();

        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @ExceptionHandler(value = {GenericWebException.class, ResponseStatusException.class})
    public ResponseEntity<Object> handleCustomExeptions(Exception ex) {

        List<String> errors = new ArrayList<>();
        ApiError apiError;

        if(ex instanceof GenericWebException){
            errors.add(ex.getStackTrace()[0].getClassName() + ": " + ex.getMessage());
            apiError = new ApiError(((GenericWebException) ex).getStatus(), ex.getLocalizedMessage(), errors);
        }else{
            errors.add(ex.getStackTrace()[0].getClassName() + ": " + ((ResponseStatusException)ex).getReason());
            apiError = new ApiError(((ResponseStatusException) ex).getStatus(), ex.getLocalizedMessage(), errors);
        }

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> SQLIntegrityExceptions(Exception ex) {

        List<String> errors = new ArrayList<>();
        ApiError apiError;

        errors.add(ex.getLocalizedMessage());
        apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<>();
        ApiError apiError;

        //errors.add(ex.getLocalizedMessage());
        for (ObjectError error : ex.getAllErrors()) {
            errors.add(error.getObjectName()+ " : " + error.getDefaultMessage());
        }

        apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }
}
