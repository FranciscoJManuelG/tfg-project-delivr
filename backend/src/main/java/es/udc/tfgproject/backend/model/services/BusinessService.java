package es.udc.tfgproject.backend.model.services;

import java.util.List;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface BusinessService {

	Company addCompany(Long userId, String name, int capacity, Boolean reserve, Boolean homeSale, int reservePercentage,
			Long companyCategoryId) throws InstanceNotFoundException;

	Company modifyCompany(Long userId, Long companyId, String name, int capacity, Boolean reserve, Boolean homeSale,
			int reservePercentage, Long companyCategoryId) throws InstanceNotFoundException, PermissionException;

	Company blockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	Company unlockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	void deregister(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	Company findCompany(Long userId) throws InstanceNotFoundException;

	List<CompanyCategory> findAllCompanyCategories();

	CompanyAddress addCompanyAddress(String street, String cp, Long cityId, Long companyId)
			throws InstanceNotFoundException;

	void deleteCompanyAddress(Long userId, Long addressId) throws InstanceNotFoundException, PermissionException;

	Block<CompanyAddress> findCompanyAddresses(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

	List<City> findAllCities();

}
