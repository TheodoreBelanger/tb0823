package com.application.tb0823.toolrental.error;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Theodore Belanger
 * This is a standard error response for error handling
 */
@Getter
@Setter
public class ErrorResponse {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private int code;

    private String status;

    private String message;

    private Object data;

    /**
     * Default constructor, inserts current time-stamp 
     */
    public ErrorResponse() {
        timestamp = new Date();
    }

    
    /**
     * Additional Constructor
     * @param httpStatus
     * @param message
     */
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }


    /**
     * Additional Constructor
     * @param httpStatus
     * @param message
     * @param data
     */
    public ErrorResponse(HttpStatus httpStatus, String message, Object data) {
        this(httpStatus, message);
        this.data = data;
    }
}
