package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.CityConversor.toCityDto;
import static es.udc.tfgproject.backend.rest.dtos.CityConversor.toCityDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyAddressConversor.toCompanyAddressDto;
import static es.udc.tfgproject.backend.rest.dtos.CompanyAddressConversor.toCompanyAddressSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyCategoryConversor.toCompanyCategoryDto;
import static es.udc.tfgproject.backend.rest.dtos.CompanyCategoryConversor.toCompanyCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyConversor.toCompanyDto;
import static es.udc.tfgproject.backend.rest.dtos.GoalConversor.toGoalDto;
import static es.udc.tfgproject.backend.rest.dtos.GoalConversor.toGoalSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.GoalTypeConversor.toGoalTypeDtos;
import static es.udc.tfgproject.backend.rest.dtos.ProvinceConversor.toProvinceDto;
import static es.udc.tfgproject.backend.rest.dtos.ProvinceConversor.toProvinceDtos;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.DiscountTicket.DiscountType;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.rest.dtos.AddCityParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyAddressParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyCategoryParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddGoalParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddProvinceParamsDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CityDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyAddressDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyAddressSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyDto;
import es.udc.tfgproject.backend.rest.dtos.GoalDto;
import es.udc.tfgproject.backend.rest.dtos.GoalSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.GoalTypeDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyCityParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyCompanyCategoryParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyCompanyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyGoalParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyProvinceParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyStateGoalParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ProvinceDto;

@RestController
@RequestMapping("/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	@PostMapping("/companies")
	public CompanyDto addCompany(@RequestAttribute Long userId, @Validated @RequestBody AddCompanyParamsDto params)
			throws InstanceNotFoundException {

		return toCompanyDto(businessService.addCompany(userId, params.getName(), params.getCapacity(),
				params.getReserve(), params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId(),
				LocalTime.parse(params.getOpeningTime()), LocalTime.parse(params.getClosingTime()),
				LocalTime.parse(params.getLunchTime()), LocalTime.parse(params.getDinerTime())));

	}

	@PutMapping("/companies/{companyId}")
	public CompanyDto modifyCompany(@RequestAttribute Long userId, @PathVariable Long companyId,
			@Validated @RequestBody ModifyCompanyParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyDto(businessService.modifyCompany(userId, companyId, params.getName(), params.getCapacity(),
				params.getReserve(), params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId(),
				LocalTime.parse(params.getOpeningTime()), LocalTime.parse(params.getClosingTime()),
				LocalTime.parse(params.getLunchTime()), LocalTime.parse(params.getDinerTime())));

	}

	@PostMapping("/companies/{companyId}/block")
	public CompanyDto blockCompany(@RequestAttribute Long userId, @PathVariable Long companyId)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyDto(businessService.blockCompany(userId, companyId));

	}

	@PostMapping("/companies/{companyId}/unlock")
	public CompanyDto unlockCompany(@RequestAttribute Long userId, @PathVariable Long companyId)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyDto(businessService.unlockCompany(userId, companyId));

	}

	@DeleteMapping("/companies/{companyId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deregister(@RequestAttribute Long userId, @PathVariable Long companyId)
			throws InstanceNotFoundException, PermissionException {

		businessService.deregister(userId, companyId);

	}

	@GetMapping("/companies/company")
	public CompanyDto findCompany(@RequestAttribute Long userId) throws InstanceNotFoundException {
		Company company = businessService.findCompany(userId);

		if (company != null) {
			return toCompanyDto(company);
		} else {
			return null;
		}

	}

	@GetMapping("/companies/{companyId}")
	public CompanyDto findCompanyById(@RequestAttribute Long userId, @PathVariable Long companyId)
			throws InstanceNotFoundException {
		Company company = businessService.findCompanyById(userId, companyId);

		if (company != null) {
			return toCompanyDto(company);
		} else {
			return null;
		}

	}

	@GetMapping("/companies/categories")
	public List<CompanyCategoryDto> findAllCompanyCategories() {
		return toCompanyCategoryDtos(businessService.findAllCompanyCategories());
	}

	@PostMapping("/companyCategories")
	public CompanyCategoryDto addCompanyCategory(@RequestAttribute Long userId,
			@Validated @RequestBody AddCompanyCategoryParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyCategoryDto(businessService.addCompanyCategory(userId, params.getName()));

	}

	@PutMapping("/companyCategories/{companyCategoryId}")
	public CompanyCategoryDto modifyCompanyCategory(@RequestAttribute Long userId, @PathVariable Long companyCategoryId,
			@Validated @RequestBody ModifyCompanyCategoryParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyCategoryDto(businessService.modifyCompanyCategory(userId, companyCategoryId, params.getName()));

	}

	@PostMapping("/companyAddresses")
	public CompanyAddressDto addCompanyAddress(@Validated @RequestBody AddCompanyAddressParamsDto params)
			throws InstanceNotFoundException {

		return toCompanyAddressDto(businessService.addCompanyAddress(params.getStreet(), params.getCp(),
				params.getCityId(), params.getCompanyId()));

	}

	@DeleteMapping("/companyAddresses/{addressId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompanyAddress(@RequestAttribute Long userId, @PathVariable Long addressId)
			throws InstanceNotFoundException, PermissionException {

		businessService.deleteCompanyAddress(userId, addressId);

	}

	@GetMapping("/companyAddresses")
	public BlockDto<CompanyAddressSummaryDto> findCompanyAddresses(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam Long companyId)
			throws InstanceNotFoundException, PermissionException {

		Block<CompanyAddress> addressBlock = businessService.findCompanyAddresses(userId, companyId, page, 10);

		return new BlockDto<>(toCompanyAddressSummaryDtos(addressBlock.getItems()), addressBlock.getExistMoreItems());

	}

	@GetMapping("/cities")
	public List<CityDto> findAllCities() {
		return toCityDtos(businessService.findAllCities());
	}

	@PostMapping("/cities")
	public CityDto addCity(@RequestAttribute Long userId, @Validated @RequestBody AddCityParamsDto params)
			throws InstanceNotFoundException, PermissionException {
		return toCityDto(businessService.addCity(userId, params.getProvinceId(), params.getName()));
	}

	@PutMapping("/cities/{cityId}")
	public CityDto modifyCity(@RequestAttribute Long userId, @PathVariable Long cityId,
			@Validated @RequestBody ModifyCityParamsDto params) throws InstanceNotFoundException, PermissionException {
		return toCityDto(businessService.modifyCity(userId, cityId, params.getProvinceId(), params.getName()));
	}

	@GetMapping("/provinces")
	public List<ProvinceDto> findAllProvinces() {
		return toProvinceDtos(businessService.findAllProvinces());
	}

	@PostMapping("/provinces")
	public ProvinceDto addProvince(@RequestAttribute Long userId, @Validated @RequestBody AddProvinceParamsDto params)
			throws InstanceNotFoundException, PermissionException {
		return toProvinceDto(businessService.addProvince(userId, params.getName()));
	}

	@PutMapping("/provinces/{provinceId}")
	public ProvinceDto modifyProvince(@RequestAttribute Long userId, @PathVariable Long provinceId,
			@Validated @RequestBody ModifyProvinceParamsDto params)
			throws InstanceNotFoundException, PermissionException {
		return toProvinceDto(businessService.modifyProvince(userId, provinceId, params.getName()));
	}

	@CrossOrigin
	@GetMapping("/companyGoals")
	public BlockDto<GoalSummaryDto> findCompanyGoals(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<Goal> goalBlock = businessService.findCompanyGoals(userId, companyId, page, Constantes.SIZE);

		return new BlockDto<>(toGoalSummaryDtos(goalBlock.getItems()), goalBlock.getExistMoreItems());

	}

	@PostMapping("/goals")
	public GoalDto addGoal(@RequestAttribute Long userId, @Validated @RequestBody AddGoalParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toGoalDto(businessService.addGoal(userId, params.getCompanyId(),
				DiscountType.valueOf(params.getDiscountType()), params.getDiscountCash(),
				params.getDiscountPercentage(), params.getGoalTypeId(), params.getGoalQuantity()));

	}

	@PutMapping("/goals/{goalId}")
	public GoalDto modifyGoal(@RequestAttribute Long userId, @PathVariable Long goalId,
			@Validated @RequestBody ModifyGoalParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toGoalDto(businessService.modifyGoal(userId, params.getCompanyId(), goalId,
				DiscountType.valueOf(params.getDiscountType()), params.getDiscountCash(),
				params.getDiscountPercentage(), params.getGoalTypeId(), params.getGoalQuantity()));

	}

	@GetMapping("/goals/{goalId}")
	public GoalDto findGoal(@RequestAttribute Long userId, @PathVariable Long goalId, @RequestParam Long companyId)
			throws InstanceNotFoundException, PermissionException {
		return toGoalDto(businessService.findGoal(userId, companyId, goalId));
	}

	@PostMapping("/goals/{goalId}/modifyStateGoal")
	public GoalDto modifyStateGoal(@RequestAttribute Long userId, @PathVariable Long goalId,
			@Validated @RequestBody ModifyStateGoalParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		System.out.println("Controller");

		return toGoalDto(businessService.modifyStateGoal(userId, params.getCompanyId(), goalId, params.getOption()));

	}

	@GetMapping("/goals/goalTypes")
	public List<GoalTypeDto> findAllGoalTypes() {
		return toGoalTypeDtos(businessService.findAllGoalTypes());
	}

}
