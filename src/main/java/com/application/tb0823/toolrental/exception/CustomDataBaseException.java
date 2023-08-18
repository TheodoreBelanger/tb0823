/**
 * 
 */
package com.application.tb0823.toolrental.exception;

/**
 * @author Theodore Belanger
 * Custom exception for errors with Tools DB
 */
public class CustomDataBaseException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1001L;

    /**
     * Default constructor
     */
    public CustomDataBaseException() {
        super();
    }
    
    /**
     * constructor takes message
     * @param message
     */
    public CustomDataBaseException(String message) {
        super(message);
    }
}
