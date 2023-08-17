package com.application.tb0823.toolrental.exception;

/**
 * @author Theodore Belanger
 * Custom exception for errors finding tool by toolcode
 */
public class ToolNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8108662940892488993L;
	
	/**
	 * Default constructor
	 */
	public ToolNotFoundException() {
		super();
	}
	
	
    /**
     * Constructor with message
     * @param message error context
     */
    public ToolNotFoundException(String message) {
    	super(message);
    }

}