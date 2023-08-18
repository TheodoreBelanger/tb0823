package com.application.tb0823.toolrental.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Hashtable;

import org.springframework.stereotype.Service;

import com.application.tb0823.toolrental.dto.RentalAgreement;
import com.application.tb0823.toolrental.dto.RentalRequest;
import com.application.tb0823.toolrental.entity.Tool;
import com.application.tb0823.toolrental.entity.ToolPurchaseData;
import com.application.tb0823.toolrental.exception.DateParseException;
import com.application.tb0823.toolrental.repo.ToolRepositoryInterface;
import com.application.tb0823.toolrental.utility.RentalAgreementCalculator;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Theodore Belanger
 * Rental Agreement Service
 */
@Service
@Slf4j
public class RentalAgreementService implements RentalAgreementInterface {
	
	private final static String CUSTOM_DATE_PATTERN = "MM/dd/yy";
	private final static String MONEY_PATTERN = "%,.2f";
	
	@Setter
	private Hashtable<String,Tool> toolsTable;
	@Setter 
	private Hashtable<String,ToolPurchaseData> toolPurchaseDataTable;
	
	private final RentalAgreementCalculator rentalAgreementCalculator;
	private final ToolRepositoryInterface toolRepository;
	
	/**
	 * Constructor
	 * @param rentalAgreementCalculator
	 */
	public RentalAgreementService(RentalAgreementCalculator rentalAgreementCalculator, ToolRepositoryInterface toolRepository) {
		this.rentalAgreementCalculator = rentalAgreementCalculator;
		this.toolRepository = toolRepository;
	}

	/**
	 * Method to process tool rentals 
	 * @param rentalRequest
	 * @return
	 */
	@Override
	public RentalAgreement processRental(RentalRequest rentalRequest) {
		Tool tool = toolRepository.getTool(rentalRequest.getToolCode());

		ToolPurchaseData toolPurchaseData = toolRepository.getToolPurchaseData(tool.getToolType());

		return generateRentalAgreement(rentalRequest, tool, toolPurchaseData);
	}


	/**
	 * Generates Rental Agreements
	 * @param rentalRequest user provided
	 * @param tool internal tool data
	 * @param toolPurchaseData  internal tool purchase data
	 * @return
	 */
	private RentalAgreement generateRentalAgreement(RentalRequest rentalRequest, Tool tool, ToolPurchaseData toolPurchaseData) {
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CUSTOM_DATE_PATTERN);
		
		LocalDate checkoutDate = parseCheckoutDate(rentalRequest, DATE_TIME_FORMATTER);
		
		int calculatedNumberOfDaysToChargeCustomer = rentalAgreementCalculator.calculateNumberOfDaysToChargeCustomer(rentalRequest.getRentalDayCount(), checkoutDate,toolPurchaseData);
		
		BigDecimal preDiscountCharge = rentalAgreementCalculator.calculatePreDiscountCharge(toolPurchaseData, calculatedNumberOfDaysToChargeCustomer);
		
		BigDecimal discountAmount = rentalAgreementCalculator.calculateDiscountAmount(rentalRequest, preDiscountCharge);
		
		BigDecimal dailyCharge = toolPurchaseData.getDailyCharge().setScale(2, RoundingMode.UP);
		
		BigDecimal finalCost = rentalAgreementCalculator.calculateFinalCost(discountAmount, dailyCharge, calculatedNumberOfDaysToChargeCustomer);
		
		return RentalAgreement.builder()
			.toolCode(rentalRequest.getToolCode())
			.toolType(tool.getToolType())
			.toolBrand(tool.getBrand())
			.rentalDays(rentalRequest.getRentalDayCount())
			.checkoutDate(checkoutDate.format(DATE_TIME_FORMATTER))
			.dueDate(checkoutDate.plusDays(rentalRequest.getRentalDayCount()).format(DATE_TIME_FORMATTER))
			.dailyRentalCharge("$" + String.format(MONEY_PATTERN, dailyCharge))
			.chargeDays(calculatedNumberOfDaysToChargeCustomer)
			.preDiscountCharge("$" + String.format(MONEY_PATTERN, preDiscountCharge))
			.discountPercent(BigDecimal.valueOf(rentalRequest.getDiscountPercent()) + "%")
			.discountAmount("$" +  String.format(MONEY_PATTERN, discountAmount))
			.finalCharge("$" + String.format(MONEY_PATTERN, finalCost))
			.build();
	}

	/**
	 * Parses checkout data with given formatter
	 * @param rentalRequest
	 * @param DATE_TIME_FORMATTER
	 * @param checkoutDate
	 * @return localDate parsed
	 */
	private LocalDate parseCheckoutDate(RentalRequest rentalRequest, DateTimeFormatter DATE_TIME_FORMATTER) {
		try {
			return LocalDate.parse(rentalRequest.getCheckoutDate(), DATE_TIME_FORMATTER);
		} catch (DateTimeParseException e) {
			log.error(e.getMessage());
			throw new DateParseException(e.getMessage());
		}
	}
	
}
