package com.application.tb0823.toolrental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.application.tb0823.toolrental.dto.RentalAgreement;
import com.application.tb0823.toolrental.dto.RentalRequest;
import com.application.tb0823.toolrental.entity.Tool;
import com.application.tb0823.toolrental.entity.ToolPurchaseData;
import com.application.tb0823.toolrental.exception.ToolNotFoundException;
import com.application.tb0823.toolrental.exception.ToolPurchaseDataNotFoundException;
import com.application.tb0823.toolrental.repo.ToolRepositoryInterface;
import com.application.tb0823.toolrental.service.RentalAgreementInterface;
import com.application.tb0823.toolrental.service.RentalAgreementService;
import com.application.tb0823.toolrental.utility.RentalAgreementCalculator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @author Theodore Belanger
 *	Tests for Tool Rental App
 */
@SpringBootTest
class ToolRentalApplicationTests {

    private RentalAgreementCalculator rentalAgreementCalculator = new RentalAgreementCalculator();;

    @Mock
    private ToolRepositoryInterface toolRepository;
    
    //--------------------Validation Tests---------------------------
    
    @Test
    public void validShouldPassValidation() {
    	// Given
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
    	RentalRequest rentalRequest = new RentalRequest(
        	"JAKR",//valid tooCode
        	10,//valid daysToRent
        	20,//valid discountPercent
        	"03/08/23"//valid rental date format
    	);
    	
    	//When validation is run for API/CLI Request
        Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);
        
        //Then
        assertTrue(violations.isEmpty());
    }
    
    @Test
    public void invalidToolCodeShouldNotPassValidation() {
    	// Given
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
    	RentalRequest rentalRequest = new RentalRequest(
        	"",//invalid tooCode
        	10,//valid daysToRent
        	20,//valid discountPercent
        	"03/08/23"//valid rental date format
    	);
    	
    	//When validation is run for API/CLI Request
        Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);
        
        //Then
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        assertEquals("toolCode can not be empty",((ConstraintViolation<RentalRequest>) violations.toArray()[0]).getMessage());
    }
    
    
    //Additional test for discount percent lower bound violation
    @Test
    public void invalidDiscountPercent2ShouldFailValidation() {
    	// Given
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
    	RentalRequest rentalRequest = new RentalRequest(
    		"JAKR",//valid tooCode
    		10,//validDaysToRent
    		-2,//invalid discountPercent!
    		"03/08/23"//valid date rental
        );
      
    	//When validation is run for API/CLI Request
    	Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);

    	//Then
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        assertEquals("discountPercent must be a value in (0-100)",((ConstraintViolation<RentalRequest>) violations.toArray()[0]).getMessage());
    }
    
    @Test
    public void invalidCheckoutDateFormatShouldFailValidation() {
    	// Given
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		RentalRequest rentalRequest = new RentalRequest(
			"JAKR",//valid tooCode
			10,//validDaysToRent
			7,//valid discountPercent
			"3-8-2023"//invalid date rental!
        );
		
		//When validation is run for API/CLI Request
		Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        assertEquals("invalid date format, example: 07/04/23",((ConstraintViolation<RentalRequest>) violations.toArray()[0]).getMessage());
    }
    
    /**This test show upper bound for rental days, note this value is so high to allow 
     * final cost to be in the thousands to show proper formatting is provided
     */
    @Test
    public void invalidRentalDayCountShouldFailValidation() {
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
    	
    	// Given
      RentalRequest rentalRequest = new RentalRequest(
          "JAKR",//valid tool code
          10001,//invalid daysToRent, limit 10,000 days
          99,//discountPercent
          "03/08/23"//valid checkout date
      );
		Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        assertEquals("rentalDayCount must be a value in (1-10000)",((ConstraintViolation<RentalRequest>) violations.toArray()[0]).getMessage());
    }
    
    //-----------------------------Custom Exception tests---------------------------
    @Test
    void should_throw_exception_when_tool_purchase_Data_not_found() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "JAKR",
            10,
            20,
            "03/08/2023"
        );
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid");
	
        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenThrow( new ToolPurchaseDataNotFoundException("ToolPurchaseData with tool code " + tool.getToolType() + " not found"));

        //When/Then
        assertThrows(ToolPurchaseDataNotFoundException.class, () -> new RentalAgreementService(rentalAgreementCalculator, toolRepository).processRental(rentalRequest));
    }
    
    @Test
    void should_throw_exception_when_tool_not_found() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "JAKR",
            10,
            20,
            "03/08/2023"
        );
        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenThrow( new ToolNotFoundException("Tool with tool code " + rentalRequest.getToolCode() + " not found"));;

        //When/Then
        assertThrows(ToolNotFoundException.class, () -> new RentalAgreementService(rentalAgreementCalculator, toolRepository).processRental(rentalRequest));
    }
    
    //---------------------Requested Tests-----------------

    @Test
    public void test_1_invalid_discount_percent_should_fail_validation() {
		// Given
    	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		RentalRequest rentalRequest = new RentalRequest(
				"JAKR",//valid tooCode
				5,//valid daysToRent
				101,//invalid discountPercent!
				"03/09/15"//valid rental date format
		);
		
    	//When validation is run for API/CLI Request
		Set<ConstraintViolation<RentalRequest>> violations = validator.validate(rentalRequest);

		//Then
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        assertEquals("discountPercent must be a value in (0-100)",((ConstraintViolation<RentalRequest>) violations.toArray()[0]).getMessage());
    }
    
    @Test
    void test_2_should_generate_rental_agreement_with_correct_details() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "LADW",//toolCode
            3,//rentalDays
            10,//discountPercent
            "07/02/20"//checkout date
        );
        Tool tool = new Tool("LADW", "Ladder", "Werner");
        ToolPurchaseData toolPurchaseData = new ToolPurchaseData("Ladder", BigDecimal.valueOf(1.99), true, true, false);

        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenReturn(toolPurchaseData);

        RentalAgreementInterface rentalService = new RentalAgreementService(rentalAgreementCalculator, toolRepository);
        
        // When
        RentalAgreement rentalAgreement = rentalService.processRental(rentalRequest);

        // Then
        assertEquals("LADW", rentalAgreement.getToolCode());
        assertEquals("Ladder", rentalAgreement.getToolType());
        assertEquals("Werner", rentalAgreement.getToolBrand());
	    assertEquals(3, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate());
        assertEquals("07/05/20", rentalAgreement.getDueDate());
        assertEquals("$1.99", rentalAgreement.getDailyRentalCharge());
        assertEquals(2, rentalAgreement.getChargeDays());
        assertEquals("$3.98", rentalAgreement.getPreDiscountCharge());
        assertEquals("10%", rentalAgreement.getDiscountPercent());
        assertEquals("$0.40", rentalAgreement.getDiscountAmount());
        assertEquals("$3.58", rentalAgreement.getFinalCharge());
    }
    
    
    @Test
    void test_3_should_generate_rental_agreement_with_correct_details() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "CHNS",//toolCode
            5,//daysRental
            25,//discountPercent
            "07/02/15"//checkoutDate
        );
        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl");
        ToolPurchaseData toolPurchaseData = new ToolPurchaseData("Chainsaw", BigDecimal.valueOf(1.49), true, false, true);

        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenReturn(toolPurchaseData);

        RentalAgreementInterface rentalService = new RentalAgreementService(rentalAgreementCalculator, toolRepository);
        
        // When
        RentalAgreement rentalAgreement = rentalService.processRental(rentalRequest);

        // Then
        assertEquals("CHNS", rentalAgreement.getToolCode());
        assertEquals("Chainsaw", rentalAgreement.getToolType());
        assertEquals("Stihl", rentalAgreement.getToolBrand());
	    assertEquals(5, rentalAgreement.getRentalDays());
        assertEquals("07/02/15", rentalAgreement.getCheckoutDate());
        assertEquals("07/07/15", rentalAgreement.getDueDate());
        assertEquals("$1.49", rentalAgreement.getDailyRentalCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals("$4.47", rentalAgreement.getPreDiscountCharge());
        assertEquals("25%", rentalAgreement.getDiscountPercent());
        assertEquals("$1.12", rentalAgreement.getDiscountAmount());
        assertEquals("$3.35", rentalAgreement.getFinalCharge());
    }

    
    @Test
    void test_4_should_generate_rental_agreement_with_correct_details() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "JAKD",
            6,
            0,
            "09/03/15"
        );
        Tool tool = new Tool("JAKD", "Jackhammer", "DeWalt");
        ToolPurchaseData toolPurchaseData = new ToolPurchaseData("Jackhammer", BigDecimal.valueOf(2.99), true, false, false);

        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenReturn(toolPurchaseData);

        RentalAgreementInterface rentalService = new RentalAgreementService(rentalAgreementCalculator, toolRepository);
        
        // When
        RentalAgreement rentalAgreement = rentalService.processRental(rentalRequest);

        // Then
        assertEquals("JAKD", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("DeWalt", rentalAgreement.getToolBrand());
	    assertEquals(6, rentalAgreement.getRentalDays());
        assertEquals("09/03/15", rentalAgreement.getCheckoutDate());
        assertEquals("09/09/15", rentalAgreement.getDueDate());
        assertEquals("$2.99", rentalAgreement.getDailyRentalCharge());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals("$8.97", rentalAgreement.getPreDiscountCharge());
        assertEquals("0%", rentalAgreement.getDiscountPercent());
        assertEquals("$0.00", rentalAgreement.getDiscountAmount());
        assertEquals("$8.97", rentalAgreement.getFinalCharge());
    }
    
    @Test
    void should_generate_rental_agreement_with_correct_details_for_test_5() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "JAKR",
            9,
            0,
            "07/02/20"
        );
        Tool tool = new Tool("JAKR", "Jackhammer", "DeWalt");
        ToolPurchaseData toolPurchaseData = new ToolPurchaseData("Jackhammer", BigDecimal.valueOf(2.99), true, false, true);

        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenReturn(toolPurchaseData);

        RentalAgreementInterface rentalService = new RentalAgreementService(rentalAgreementCalculator, toolRepository);
        
        // When
        RentalAgreement rentalAgreement = rentalService.processRental(rentalRequest);

        // Then
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("DeWalt", rentalAgreement.getToolBrand());
	    assertEquals(9, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate());
        assertEquals("07/11/20", rentalAgreement.getDueDate());
        assertEquals("$2.99", rentalAgreement.getDailyRentalCharge());
        assertEquals(6, rentalAgreement.getChargeDays());
        assertEquals("$17.94", rentalAgreement.getPreDiscountCharge());
        assertEquals("0%", rentalAgreement.getDiscountPercent());
        assertEquals("$0.00", rentalAgreement.getDiscountAmount());
        assertEquals("$17.94", rentalAgreement.getFinalCharge());
    }
    
    @Test
    void should_generate_rental_agreement_with_correct_details_for_test_6() {
        // Given
        RentalRequest rentalRequest = new RentalRequest(
            "JAKR",//toolCode
            4,//rentalDays
            50,//percentDiscount
            "07/02/20"//checkout date
        );
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid");
        ToolPurchaseData toolPurchaseData = new ToolPurchaseData("Jackhammer", BigDecimal.valueOf(2.99), true, false, false);

        Mockito.when(toolRepository.getTool(rentalRequest.getToolCode())).thenReturn(tool);
        Mockito.when(toolRepository.getToolPurchaseData(tool.getToolType())).thenReturn(toolPurchaseData);

        RentalAgreementInterface rentalService = new RentalAgreementService(rentalAgreementCalculator, toolRepository);
        
        // When
        RentalAgreement rentalAgreement = rentalService.processRental(rentalRequest);

        // Then
        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());
	    assertEquals(4, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate());
        assertEquals("07/06/20", rentalAgreement.getDueDate());
        assertEquals("$2.99", rentalAgreement.getDailyRentalCharge());
        assertEquals(1, rentalAgreement.getChargeDays());
        assertEquals("$2.99", rentalAgreement.getPreDiscountCharge());
        assertEquals("50%", rentalAgreement.getDiscountPercent());
        assertEquals("$1.50", rentalAgreement.getDiscountAmount());
        assertEquals("$1.49", rentalAgreement.getFinalCharge());
    }

}
