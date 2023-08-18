/**
 * 
 */
package com.application.tb0823.toolrental.exception;

/**
 * @author Theodore Belanger
 * Custom exception for errors with Tools DB
 */
public class DateParseException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1001L;

    /**
     * Default constructor
     */
    public DateParseException() {
        super();
    }
    
    /**
     * constructor takes message
     * @param message
     */
    public DateParseException(String message) {
        super(message);
    }
}
