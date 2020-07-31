package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.CityConversor.toCityDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyAddressConversor.toCompanyAddressDto;
import static es.udc.tfgproject.backend.rest.dtos.CompanyAddressConversor.toCompanyAddressSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyCategoryConversor.toCompanyCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyConversor.toCompanyDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyAddressParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CityDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyAddressDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyAddressSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyCompanyParamsDto;

@RestController
@RequestMapping("/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	@PostMapping("/companies")
	public CompanyDto addCompany(@RequestAttribute Long userId, @Validated @RequestBody AddCompanyParamsDto params)
			throws InstanceNotFoundException {

		return toCompanyDto(
				businessService.addCompany(userId, params.getName(), params.getCapacity(), params.getReserve(),
						params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId()));

	}

	@PutMapping("/companies/{companyId}")
	public CompanyDto modifyCompany(@RequestAttribute Long userId, @PathVariable Long companyId,
			@Validated @RequestBody ModifyCompanyParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toCompanyDto(businessService.modifyCompany(userId, companyId, params.getName(), params.getCapacity(),
				params.getReserve(), params.getHomeSale(), params.getReservePercentage(),
				params.getCompanyCategoryId()));

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

	@GetMapping("/companies/categories")
	public List<CompanyCategoryDto> findAllCompanyCategories() {
		return toCompanyCategoryDtos(businessService.findAllCompanyCategories());
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

}
