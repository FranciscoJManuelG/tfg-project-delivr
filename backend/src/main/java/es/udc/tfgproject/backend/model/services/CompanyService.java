package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;

public interface CompanyService {

	Company addCompany(Long userId, String name, int capacity, Boolean reserve, Boolean homeSale, int reservePercentage,
			Long companyCategoryId) throws InstanceNotFoundException;

	Company modifyCompany(Long userId, Long companyId, String name, int capacity, Boolean reserve, Boolean homeSale,
			int reservePercentage, Long companyCategoryId) throws InstanceNotFoundException, WrongUserException;

	void deregister(Long userId, Long companyId) throws InstanceNotFoundException, WrongUserException;

}
