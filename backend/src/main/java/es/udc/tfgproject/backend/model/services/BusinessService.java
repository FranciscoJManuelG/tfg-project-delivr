package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.util.List;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.DiscountTicket.DiscountType;
import es.udc.tfgproject.backend.model.entities.Goal;
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

	Goal addGoal(Long userId, Long companyId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException;

	Goal modifyGoal(Long userId, Long companyId, Long goalId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException;

	Goal modifyStateGoal(Long userId, Long companyId, Long goalId, String option)
			throws InstanceNotFoundException, PermissionException;

	Block<Goal> findCompanyGoals(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

}
