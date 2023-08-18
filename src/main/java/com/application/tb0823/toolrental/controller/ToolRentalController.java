/**
 * 
 */
package com.application.tb0823.toolrental.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.tb0823.toolrental.dto.RentalAgreement;
import com.application.tb0823.toolrental.dto.RentalRequest;
import com.application.tb0823.toolrental.repo.ToolRepository;
import com.application.tb0823.toolrental.repo.ToolRepositoryInterface;
import com.application.tb0823.toolrental.service.RentalAgreementInterface;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Theodore Belanger
 * Primary Controller for Tool Rental App
 */
@Tag(name = "ToolRentals", description = "Tool Rental API")
@Validated
@RestController
@Slf4j
public class ToolRentalController {

    //safe dependency injection via constructor
    private final RentalAgreementInterface rentalAgreementService;
    
    //Special Case of repo in controller, used to reset cache directly in-order to keep service unaware
    private final ToolRepositoryInterface toolRepository;
    
    /**
     * Constructor
     * @param rentalAgreementServiceInterface
     * @param toolRepositoryInterface
     */
    public ToolRentalController(RentalAgreementInterface rentalAgreementService, ToolRepositoryInterface toolRepository) {
        this.rentalAgreementService = rentalAgreementService;
        this.toolRepository = toolRepository;
    }

    /**
     * Primary checkout method for Tool Rental App
     * @param rentalRequest to checkout
     * @return rental agreement or user friendly error handled by global exception handler
     */
    @RequestMapping(path ="/checkout", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public RentalAgreement checkout(@Valid @ModelAttribute RentalRequest rentalRequest) {
        log.info("/checkout called with RentalRequest:" + System.lineSeparator() + rentalRequest);

        RentalAgreement rentalAgreement = rentalAgreementService.processRental(rentalRequest);
        
        log.info("/checkout returning RentalAgreement: " + System.lineSeparator() + rentalAgreement);
        return rentalAgreement;
    }
    
    /**
     * Used to reset Repo Cache of Tool and associated Purchase data
     * Use when new tool and associated purchase data is added to the App
     */
    @RequestMapping(path = "/reset-tool-cache", method = GET)
    public void resetToolCache() {
      // Clear the tool & purchaseData cache.
      toolRepository.clearCache();
    }
    
}
