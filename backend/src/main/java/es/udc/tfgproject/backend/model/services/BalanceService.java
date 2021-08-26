package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

public interface BalanceService {

	void payWeeklyBalanceToCompanies();

	void payQuarterlyFee(Long userId) throws InstanceNotFoundException;

}
