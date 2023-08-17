/**
 * 
 */
package com.application.tb0823.toolrental.utility;

import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.application.tb0823.toolrental.dto.RentalRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @author Theodore Belanger
 * CLI Tool Rental Utility
 */
@Component
public class CliToolRentalUtility {

	/**
	 * Converts CLI args to rental Request
	 * @param args passed for cli app
	 * @return rental request
	 * @throws NumberFormatException
	 */
	public RentalRequest convertArgsToServiceRequestDto(String... args) throws NumberFormatException {
		if (args.length != 5) {
		    System.err.println("Proper Usage is: java_program_filename commandline toolCode rentalDayCount discountPercent checkoutDate");
		    System.exit(0);
		  }
		
		return convertInputToRentalRequest(args);
	}

	/**
	 * Manual validation check
	 * @param rentalRequest
	 * @return set of violations if found
	 */
	public Set<ConstraintViolation<RentalRequest>> validateRentalRequest(RentalRequest rentalRequest) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);
		return violations;
	}

	/**
	 * Converts CLI args to rental Request
	 * @param args
	 * @return
	 * @throws NumberFormatException
	 */
	private RentalRequest convertInputToRentalRequest(String... args) throws NumberFormatException {
		//Throws:NumberFormatException - if the string does not contain a parsable integer.
		String toolCode = Objects.requireNonNull(args[1], "toolCode cannot be null");
		//Throws custom null pointer exception
		int rentalDayCount = Objects.requireNonNull(Integer.parseInt(args[2]), "rentalDayCount cannot be null");
		//Throws custom null pointer exception
		int discountPercent = Objects.requireNonNull(Integer.parseInt(args[3]), "discountPercent cannot be null");
		String checkoutDate = Objects.requireNonNull(args[4], "checkoutDate cannot be null");
		
		RentalRequest order = RentalRequest.builder()
				.toolCode(toolCode)
				.rentalDayCount(rentalDayCount)
				.discountPercent(discountPercent)
				.checkoutDate(checkoutDate)
				.build();
		return order;
	}
}
