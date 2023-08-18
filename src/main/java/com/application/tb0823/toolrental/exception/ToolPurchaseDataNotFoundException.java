package com.application.tb0823.toolrental.exception;

/**
 * @author Theodore Belanger
 * Custom exception for errors finding tool purchase data by toolType
 */
public class ToolPurchaseDataNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8108662940892488499L;
    
    /**
     * Default constructor
     */
    public ToolPurchaseDataNotFoundException() {
        super();
    }
    
    
    /**
     * Constructor with message
     * @param message error context
     */
    public ToolPurchaseDataNotFoundException(String message) {
        super(message);
    }

}