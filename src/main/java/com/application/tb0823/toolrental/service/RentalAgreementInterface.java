package com.application.tb0823.toolrental.service;

import com.application.tb0823.toolrental.dto.RentalAgreement;
import com.application.tb0823.toolrental.dto.RentalRequest;

public interface RentalAgreementInterface {

	/**
	 * Method to process tool rentals 
	 * @param rentalRequest
	 * @return generated rental agreement
	 */
	RentalAgreement processRental(RentalRequest rentalRequest);

}