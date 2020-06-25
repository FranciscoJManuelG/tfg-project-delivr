package es.udc.tfgproject.backend.model.services;

import java.util.List;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;

public interface BusinessService {

	Company addCompany(Long userId, String name, int capacity, Boolean reserve, Boolean homeSale, int reservePercentage,
			Long companyCategoryId) throws InstanceNotFoundException;

	Company modifyCompany(Long userId, Long companyId, String name, int capacity, Boolean reserve, Boolean homeSale,
			int reservePercentage, Long companyCategoryId) throws InstanceNotFoundException, WrongUserException;

	void deregister(Long userId, Long companyId) throws InstanceNotFoundException, WrongUserException;

	List<CompanyCategory> findAllCompanyCategories();

	Address addAddress(String street, String cp, Long cityId, Long companyId) throws InstanceNotFoundException;

	void deleteAddress(Long addressId) throws InstanceNotFoundException;

	Block<CompanyAddress> findAddresses(Long companyId, int page, int size);

	List<City> findAllCities();

}
