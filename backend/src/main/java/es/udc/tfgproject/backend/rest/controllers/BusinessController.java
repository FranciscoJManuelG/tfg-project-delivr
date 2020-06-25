package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.AddressConversor.toAddressDto;
import static es.udc.tfgproject.backend.rest.dtos.AddressConversor.toAddressSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CityConversor.toCityDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyCategoryConversor.toCompanyCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyConversor.toCompanyDto;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.dtos.AddAddressParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddressDto;
import es.udc.tfgproject.backend.rest.dtos.AddressSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CityDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyDto;

@RestController
@RequestMapping("/business")
public class BusinessController {

	private final static String WRONG_USER_EXCEPTION_CODE = "project.exceptions.WrongUserException";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BusinessService companyService;

	@ExceptionHandler(WrongUserException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handleWrongUserException(WrongUserException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(WRONG_USER_EXCEPTION_CODE, null, WRONG_USER_EXCEPTION_CODE,
				locale);

		return new ErrorsDto(errorMessage);

	}

	@PostMapping("/companies")
	public CompanyDto addCompany(@RequestAttribute Long userId, @Validated @RequestBody AddCompanyParamsDto params)
			throws InstanceNotFoundException {

		return toCompanyDto(
				companyService.addCompany(userId, params.getName(), params.getCapacity(), params.getReserve(),
						params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId()));

	}

	/* DUDA : mejor crear otro ParamsDto aunque sea igual, o así es válido */
	@PutMapping("/companies/{id}")
	public CompanyDto modifyCompany(@RequestAttribute Long userId, @PathVariable Long id,
			@Validated @RequestBody AddCompanyParamsDto params) throws WrongUserException, InstanceNotFoundException {

		return toCompanyDto(
				companyService.modifyCompany(userId, id, params.getName(), params.getCapacity(), params.getReserve(),
						params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId()));

	}

	@PostMapping("/companies/{id}/deregister")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deregister(@RequestAttribute Long userId, @PathVariable Long id)
			throws InstanceNotFoundException, WrongUserException {

		companyService.deregister(userId, id);

	}

	// TODO : deregister marcar estado a no disponible y crear otro método para
	// elimianar la Company

	@GetMapping("/companies/categories")
	public List<CompanyCategoryDto> findAllCompanyCategories() {
		return toCompanyCategoryDtos(companyService.findAllCompanyCategories());
	}

///global/companies/{companyId}/addresses
///global/addresses
	@PostMapping("/addresses")
	public AddressDto addAddress(@PathVariable Long companyId, @Validated @RequestBody AddAddressParamsDto params)
			throws InstanceNotFoundException {

		return toAddressDto(
				companyService.addAddress(params.getStreet(), params.getCp(), params.getCityId(), companyId));

	}

	@DeleteMapping("/addresses/{addressId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAddress(@PathVariable Long addressId) throws InstanceNotFoundException {

		companyService.deleteAddress(addressId);

	}

	@GetMapping("/addresses/find")
	public BlockDto<AddressSummaryDto> findAddresses(@PathVariable Long companyId,
			@RequestParam(defaultValue = "0") int page) {

		Block<CompanyAddress> addressBlock = companyService.findAddresses(companyId, page, 10);

		return new BlockDto<>(toAddressSummaryDtos(addressBlock.getItems()), addressBlock.getExistMoreItems());

	}

	@GetMapping("/cities")
	public List<CityDto> findAllCities() {
		return toCityDtos(companyService.findAllCities());
	}

}
