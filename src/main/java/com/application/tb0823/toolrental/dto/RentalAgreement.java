package com.application.tb0823.toolrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Theodore Belanger
 * Rental Agreement Class
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RentalAgreement {
    
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final int rentalDays;
    private final String checkoutDate;
    private final String dueDate;
    private final String dailyRentalCharge;
    private final int chargeDays;
    private final String preDiscountCharge;
    private final String discountPercent;
    private final String discountAmount;
    private final String finalCharge;
    
    /**
     * To string method
     */
    @Override
    public String toString() {
        return  "Tool Code: " + toolCode + System.lineSeparator() + 
                "Tool Type: " + toolType + System.lineSeparator() +
                "Tool Brand: " + toolBrand + System.lineSeparator() +
                "Rental Days: " + rentalDays + System.lineSeparator() +
                "Checkout Date: " + checkoutDate + System.lineSeparator() +
                "Due Date: " + dueDate +  System.lineSeparator() +
                "Daily Rental Charge: " + dailyRentalCharge + System.lineSeparator() +
                "Charge Days: " + chargeDays + System.lineSeparator() + 
                "PreDiscount Charge: " + preDiscountCharge + System.lineSeparator() +
                "Discount Percent: " + discountPercent + System.lineSeparator() +
                "Discount Amount: " + discountAmount + System.lineSeparator() +
                "Final Charge: " + finalCharge + System.lineSeparator();
    }
}
