package com.application.tb0823.toolrental.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.application.tb0823.toolrental.dto.RentalRequest;
import com.application.tb0823.toolrental.entity.ToolPurchaseData;

/**
 * @author Theodore Belanger
 * Calculator for rental agreements
 */
@Component
public class RentalAgreementCalculator {
	
	/**
	 * Calculates final cost for rental agreement
	 * @param rentalRequest user provided
	 * @param toolPurchaseData internal data
	 * @param calculatedNumberOfDaysToChargeCustomer chargeable days
	 * @return cost of rental
	 */
	public BigDecimal calculateFinalCost(BigDecimal discountAmount, BigDecimal dailyCharge, int calculatedNumberOfDaysToChargeCustomer) {
		return BigDecimal.valueOf(calculatedNumberOfDaysToChargeCustomer)
				.multiply(dailyCharge)
				.subtract(discountAmount)
				.setScale(2, RoundingMode.UP);
	}

	/**
	 * Calculates discount amount as decimal
	 * @param toolPurchaseData user provided
	 * @param calculatedNumberOfDaysToChargeCustomer chargeable days
	 * @return percent discount as decimal
	 */
	public BigDecimal calculatePreDiscountCharge(ToolPurchaseData toolPurchaseData,
			int calculatedNumberOfDaysToChargeCustomer) {
		return toolPurchaseData.getDailyCharge()
				.multiply(BigDecimal.valueOf(calculatedNumberOfDaysToChargeCustomer))
				.setScale(2, RoundingMode.UP);
	}
	
	/**
	 * Calculates discount amount
	 * @param rentalRequest provided by user
	 * @param preDiscountCharge calculated based on user input
	 * @return discount amount
	 */
	public BigDecimal calculateDiscountAmount(RentalRequest rentalRequest, BigDecimal preDiscountCharge) {
		return preDiscountCharge
				.multiply(BigDecimal.valueOf(rentalRequest.getDiscountPercent())
				.divide(BigDecimal.valueOf(100)))
				.setScale(2, RoundingMode.UP);
	}
	
	/**
	 * Calculates number of days to charge customer based on weekday, weekend, and holiday charges
	 * @param rentalDayCount user provided
	 * @param checkoutDate user provided
	 * @param toolPurchaseData user provided
	 * @return charge days
	 */
	public int calculateNumberOfDaysToChargeCustomer(int rentalDayCount, LocalDate checkoutDate, ToolPurchaseData toolPurchaseData) {
		//start with total rental days then subtract uncharged days
		int charableDays = rentalDayCount;
        for (int i = 1; i <= rentalDayCount; i++) {
        	
        	LocalDate dayIndex = checkoutDate.plusDays(i);
        	
        	//check weekdays
			charableDays = subtractWeekdayNonChargableDays(toolPurchaseData, charableDays, dayIndex);
            
            //check weekends
			charableDays = subtractWeekendNonChargableDays(toolPurchaseData, charableDays, dayIndex);

			//check holidays 
		    charableDays = subtractHolidayNonChargableDays(rentalDayCount, toolPurchaseData, charableDays, i, dayIndex);
        }
		return charableDays;
	}
	
	/**
	 * Subtracts week day days if non-chargeable
	 * @param toolPurchaseData user provided checkout date
	 * @param charableDays current chargeable days 
	 * @param dayIndex days since checkout date
	 * @return chargeable days sans non-chargeable week days
	 */
	private int subtractWeekdayNonChargableDays(ToolPurchaseData toolPurchaseData, int charableDays, LocalDate dayIndex) {
		if(!toolPurchaseData.isWeekdayCharge()) {
		    if (dayIndex.getDayOfWeek().getValue() < 6) {
		    	charableDays--;
		    }
		}
		return charableDays;
	}

	/**
	 * Subtracts weekends days if not-chargeable
	 * @param toolPurchaseData user provided checkout date
	 * @param charableDays current chargeable days 
	 * @param dayIndex days since checkout date
	 * @return chargeable days sans non-chargeable weekend days
	 */
	private int subtractWeekendNonChargableDays(ToolPurchaseData toolPurchaseData, int charableDays,
			LocalDate dayIndex) {
		if (!toolPurchaseData.isWeekendCharge()) {
		    if (dayIndex.getDayOfWeek().getValue() > 5) {
		        charableDays--;
		    }
		}
		return charableDays;
	}
	
	/**
	 * Subtracts holiday days if not-chargeable
	 * @param rentalDayCount user provided number of rental days
	 * @param toolPurchaseData internal tool purchase data
	 * @param charableDays current chargeable days
	 * @param i number of days since checkout
	 * @param dayIndex day since checkout date 
	 * @return chargeable days sans non-chargeable holidays
	 */
	private int subtractHolidayNonChargableDays(int rentalDayCount, ToolPurchaseData toolPurchaseData, int charableDays,
			int i, LocalDate dayIndex) {
		if (!toolPurchaseData.isHolidayCharge()) {
			//4th of July
			charableDays = checkForthOfJuly(rentalDayCount, charableDays, i, dayIndex);
			
			//Labor Day
			charableDays = checkLaborDay(charableDays, dayIndex);
		}
		return charableDays;
	}

	/**
	 * Calculate chargeable days subtraction for the 4th of July
	 * @param rentalDayCount amount of days of rental
	 * @param charableDays current number of chargeable days
	 * @param i number of days since checkout
	 * @param dayIndex day since rental checkout
	 * @return number of chargeable days sans 4th of July
	 */
	private int checkForthOfJuly(int rentalDayCount, int charableDays, int i, LocalDate dayIndex) {
		boolean is4thOfJuly = dayIndex.equals(LocalDate.of(dayIndex.getYear(), 7, 4));
		if (is4thOfJuly) {
			//4th of July falls on Mon-Friday
			if (dayIndex.getDayOfWeek().getValue() <= 5) {
				charableDays--;
			}
			
			// If 4th of July falls on a Saturday, then the holiday charge is applied to Friday
		    if (dayIndex.getDayOfWeek().getValue() == 6) {
		        //IF Today Saturday, its the 4th, did they rent yesterday on Friday when discount applicable
		        if(i >= 1) {
		        	charableDays--;
		        }
		    } 
		    
		    // If 4th of July falls on a Sunday, then the holiday charge is applied to Monday
		    if (dayIndex.getDayOfWeek().getValue() == 7) {
		        //if we are going to assess charges for tomorrow
		    	if(i + 1 <= rentalDayCount) {
		    		charableDays--;
		    	}
		    }
		}
		return charableDays;
	}

	/**
	 * Labor day check
	 * @param charableDays current count of chargeable days
	 * @param dayIndex days since checkout
	 * @return charableDays assessed for labor day 
	 */
	private int checkLaborDay(int charableDays, LocalDate dayIndex) {
		if(dayIndex.equals(calculateLaborDayDate(dayIndex))){
			charableDays--;
		}
		return charableDays;
	}

	/**
	 * Calculates labor day
	 * @param date of year to check
	 * @return date of labor day
	 */
	private LocalDate calculateLaborDayDate(LocalDate date) {
		int year = date.getYear();
	    int month = 9;
	    int day = 1;
	    LocalDate laborDay = LocalDate.of(year, month, day);

	    //Find first Monday in September
	    while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY) {
	        laborDay = laborDay.plusDays(1);
	    }

	    return laborDay;
	}
}