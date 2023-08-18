package com.application.tb0823.toolrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.application.tb0823.toolrental.error.ErrorResponse;
import com.application.tb0823.toolrental.exception.CustomDataBaseException;
import com.application.tb0823.toolrental.exception.DateParseException;
import com.application.tb0823.toolrental.exception.ToolNotFoundException;
import com.application.tb0823.toolrental.exception.ToolPurchaseDataNotFoundException;


/**        
 * @author Theodore Belanger
 * This Global Exception handler is leveraged by the tool rental controller
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles MethodArgumentNotValidException runtime exceptions from bad user input
     * @param ex exception thrown containing message
     * @return contains errors found in validation of user input
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Map<String,List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * handles ToolNotFoundException runtime exception from error on DB responses
     * @param ex exception thrown containing message
     * @return message of error with DB response
     */
    @ExceptionHandler(ToolNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleCustomDataBaseException(ToolNotFoundException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status,ex.getMessage()),status);
    }
    
    /**
     * handles DateParseException runtime exception from error on DB responses
     * @param ex exception thrown containing message
     * @return message of error with DB response
     */
    @ExceptionHandler(DateParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ErrorResponse> handleCustomDataBaseException(DateParseException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status,ex.getMessage()),status);
    }
    
    /**
     * handles ToolPurchaseDataNotFoundException runtime exception from error on DB responses
     * @param ex exception thrown containing message
     * @return message of error with DB response
     */
    @ExceptionHandler(ToolPurchaseDataNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleCustomDataBaseException(ToolPurchaseDataNotFoundException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status,ex.getMessage()),status);
    }
    
    /**
     * handles CustomDataBaseException runtime exception from error on DB responses
     * @param ex exception thrown containing message
     * @return message of error with DB response
     */
    @ExceptionHandler(CustomDataBaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleCustomDataBaseException(CustomDataBaseException ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status,ex.getMessage()),status);
    }
    
    /**
     * Helper method to return a map of errors
     * @param errors top map 
     * @return map of errors
     */
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
