package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.CompanyConversor.toCompanySummaryDtos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.BusinessCatalogService;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CompanySummaryDto;

@RestController
@RequestMapping("/businessCatalog")
public class BusinessCatalogController {

	@Autowired
	private BusinessCatalogService businessCatalogService;

	@GetMapping("/companies")
	public BlockDto<CompanySummaryDto> findCompanies(@RequestParam(required = false) Long companyCategoryId,
			@RequestParam(required = false) Long cityId, @RequestParam(required = false) String street,
			@RequestParam(required = false) String keywords, @RequestParam(defaultValue = "0") int page) {

		Block<CompanyAddress> companyBlock = businessCatalogService.findCompanies(companyCategoryId, cityId,
				street != null ? street.trim() : null, keywords != null ? keywords.trim() : null, page,
				Constantes.SIZE);

		return new BlockDto<>(toCompanySummaryDtos(companyBlock.getItems()), companyBlock.getExistMoreItems());

	}

	@GetMapping("/companies/findAllCompanies")
	public BlockDto<CompanySummaryDto> findAllCompanies(@RequestParam(required = false) String keywords,
			@RequestParam(defaultValue = "0") int page) {

		Block<CompanyAddress> companyBlock = businessCatalogService
				.findAllCompanies(keywords != null ? keywords.trim() : null, page, Constantes.SIZE);

		return new BlockDto<>(toCompanySummaryDtos(companyBlock.getItems()), companyBlock.getExistMoreItems());

	}

}
