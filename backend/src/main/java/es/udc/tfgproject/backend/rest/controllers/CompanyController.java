package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.CompanyCategoryConversor.toCompanyCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CompanyConversor.toCompanyDto;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.WrongUserException;
import es.udc.tfgproject.backend.model.services.CompanyService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.dtos.AddCompanyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyDto;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	private final static String WRONG_USER_EXCEPTION_CODE = "project.exceptions.WrongUserException";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CompanyService companyService;

	@ExceptionHandler(WrongUserException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handleWrongUserException(WrongUserException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(WRONG_USER_EXCEPTION_CODE, null, WRONG_USER_EXCEPTION_CODE,
				locale);

		return new ErrorsDto(errorMessage);

	}

	@PostMapping("/add")
	public CompanyDto addCompany(@RequestAttribute Long userId, @Validated @RequestBody AddCompanyParamsDto params)
			throws InstanceNotFoundException {

		return toCompanyDto(
				companyService.addCompany(userId, params.getName(), params.getCapacity(), params.getReserve(),
						params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId()));

	}

	/* DUDA : mejor crear otro ParamsDto aunque sea igual, o así es válido */
	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@RequestAttribute Long userId, @PathVariable Long id,
			@Validated @RequestBody AddCompanyParamsDto params) throws WrongUserException, InstanceNotFoundException {

		return toCompanyDto(
				companyService.modifyCompany(userId, id, params.getName(), params.getCapacity(), params.getReserve(),
						params.getHomeSale(), params.getReservePercentage(), params.getCompanyCategoryId()));

	}

	@PostMapping("/{id}/deregister")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deregister(@RequestAttribute Long userId, @PathVariable Long id)
			throws InstanceNotFoundException, WrongUserException {

		companyService.deregister(userId, id);

	}

	@GetMapping("/categories")
	public List<CompanyCategoryDto> findAllCompanyCategories() {
		return toCompanyCategoryDtos(companyService.findAllCompanyCategories());
	}

}
