package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.DiscountTicket.DiscountType;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.Province;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface BusinessService {

	Company addCompany(Long userId, String name, Integer capacity, Boolean reserve, Boolean homeSale,
			Integer reservePercentage, Long companyCategoryId, LocalTime openingTime, LocalTime closingTime,
			LocalTime lunchTime, LocalTime dinerTime) throws InstanceNotFoundException;

	Company modifyCompany(Long userId, Long companyId, String name, Integer capacity, Boolean reserve, Boolean homeSale,
			Integer reservePercentage, Long companyCategoryId, LocalTime openingTime, LocalTime closingTime,
			LocalTime lunchTime, LocalTime dinerTime) throws InstanceNotFoundException, PermissionException;

	Company blockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	Company unlockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	void deregister(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException;

	Company findCompany(Long userId) throws InstanceNotFoundException;

	Company findCompanyById(Long userId, Long companyId) throws InstanceNotFoundException;

	List<CompanyCategory> findAllCompanyCategories();

	CompanyCategory addCompanyCategory(Long userId, String name) throws InstanceNotFoundException, PermissionException;

	CompanyCategory modifyCompanyCategory(Long userId, Long companyCategoryId, String name)
			throws InstanceNotFoundException, PermissionException;

	CompanyAddress addCompanyAddress(String street, String cp, Long cityId, Long companyId)
			throws InstanceNotFoundException;

	void deleteCompanyAddress(Long userId, Long addressId) throws InstanceNotFoundException, PermissionException;

	Block<CompanyAddress> findCompanyAddresses(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

	List<City> findAllCities();

	City addCity(Long userId, Long provinceId, String name) throws InstanceNotFoundException, PermissionException;

	City modifyCity(Long userId, Long cityId, Long provinceId, String name)
			throws InstanceNotFoundException, PermissionException;

	List<Province> findAllProvinces();

	Province addProvince(Long userId, String name) throws InstanceNotFoundException, PermissionException;

	Province modifyProvince(Long userId, Long provinceId, String name)
			throws InstanceNotFoundException, PermissionException;

	Goal addGoal(Long userId, Long companyId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException;

	Goal modifyGoal(Long userId, Long companyId, Long goalId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException;

	Goal findGoal(Long userId, Long companyId, Long goalId) throws InstanceNotFoundException, PermissionException;

	Goal modifyStateGoal(Long userId, Long companyId, Long goalId, String option)
			throws InstanceNotFoundException, PermissionException;

	Block<Goal> findCompanyGoals(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

	List<GoalType> findAllGoalTypes();

}
