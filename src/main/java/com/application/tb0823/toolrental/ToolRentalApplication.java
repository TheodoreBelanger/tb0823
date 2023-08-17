package com.application.tb0823.toolrental;

import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.application.tb0823.toolrental.controller.ToolRentalController;
import com.application.tb0823.toolrental.dto.RentalRequest;
import com.application.tb0823.toolrental.utility.CliToolRentalUtility;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @author Theodore Belanger
 * SpringBoot bootstrap for Tool Rental API & cli Application
 */
@SpringBootApplication
public class ToolRentalApplication implements CommandLineRunner{

	private final ToolRentalController toolRentalController;
	
	private final CliToolRentalUtility cliToolRentalUtility;
                                               
	/**
	 * @param toolRentalController
	 */
	public ToolRentalApplication(ToolRentalController toolRentalController, CliToolRentalUtility cliToolRentalUtility) {
		this.toolRentalController = toolRentalController;
		this.cliToolRentalUtility = cliToolRentalUtility;
	}

    
	/**
	 * Main application method determines startup as web app or command line app
	 * @param args, None for Spring Boot App OR CLI Tool: java_program_filename command line toolCode rentalDayCount discountPercent checkoutDate
	 */
	public static void main(String[] args) {
        WebApplicationType appType = WebApplicationType.SERVLET;
        //check if app should run as CLI APP or web application
        if (args.length > 0) {
            if (args[0].equals("commandline")) {
                appType = WebApplicationType.NONE;
            }
        }
        SpringApplication application = new SpringApplication(ToolRentalApplication.class);
        application.setWebApplicationType(appType);
        application.run(args);
    }

	/**
	 *Run method of command line runner used to start web or cli apps
	 */
	@Valid
    @Override
    public void run(String... args) throws Exception {
    	if (args.length == 0) {
            //Application is started as a web service
        	System.out.println("Started as web app.");
        } else {
            //Application is started as CLI App
        	System.out.println("Started as command line app.");

        	RentalRequest rentalRequest = cliToolRentalUtility.convertArgsToServiceRequestDto(args);
            
            // Find rental agreement validation errors
            Set<ConstraintViolation<RentalRequest>> violations = cliToolRentalUtility.validateRentalRequest(rentalRequest);
            
            //Process valid rental request or print validation errors to standard out
            if (violations.isEmpty()) {
            	//logs input and output to standard out for CLI App
            	try {
					toolRentalController.checkout(rentalRequest);
				} catch (Exception e) {
		        	System.out.println("Error: " + e.getMessage());
				}
            } else {
                // Invalid CLI Request --> Print Errors
                for (ConstraintViolation<RentalRequest> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }

	
}
