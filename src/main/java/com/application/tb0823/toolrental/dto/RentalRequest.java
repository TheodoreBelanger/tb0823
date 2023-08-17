/**
 * 
 */
package com.application.tb0823.toolrental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Theodore Belanger
 *	Rental Request Class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {
	@NotNull(message = "toolCode can not be null")
	@NotEmpty(message= "toolCode can not be empty")
	private String toolCode;
	
	@NotNull(message = "rentalDayCount can not be null, value in (1-10000)")
	@Min(value = 1, message = "rentalDayCount must be a value in (1-10000)")
	@Max(value = 10000, message = "rentalDayCount must be a value in (1-10000)")
	int rentalDayCount;
	
	@NotNull(message = "discountPercent can not be null, value in (0-100)")
	@Min(value = 0, message = "discountPercent must be a value in (0-100)")
	@Max(value = 100, message = "discountPercent must be a value in (0-100)")
	int discountPercent;
	
	@NotNull(message = "checkoutDate can not be null, example: 07-04-23")
	@Pattern(message = "invalid date format, example: 07/04/23" ,
			 regexp="^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/([0-9]{2})$")
	private String  checkoutDate;
	
	@Override
	public String toString() {
		return  "Tool Code: " + toolCode + System.lineSeparator() + 
				"Rental Day Count : " + rentalDayCount + System.lineSeparator() +
				"Discount Percent: " + discountPercent + System.lineSeparator() +
				"Checkout Date: " + checkoutDate + System.lineSeparator();
	}
}
