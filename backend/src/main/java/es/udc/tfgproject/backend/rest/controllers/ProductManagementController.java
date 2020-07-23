package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.ProductCategoryConversor.toProductCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.ProductConversor.toProductDto;
import static es.udc.tfgproject.backend.rest.dtos.ProductConversor.toProductSummaryDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.ProductManagementService;
import es.udc.tfgproject.backend.rest.dtos.AddProductParamsDto;
import es.udc.tfgproject.backend.rest.dtos.EditProductParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ProductCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.ProductDto;
import es.udc.tfgproject.backend.rest.dtos.ProductSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.StateProductParamsDto;

@RestController
@RequestMapping("/management")
public class ProductManagementController {

	@Autowired
	private ProductManagementService productManagementService;

	@PostMapping("/products")
	public ProductDto addProduct(@RequestAttribute Long userId, @Validated @RequestBody AddProductParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.addProduct(userId, params.getCompanyId(), params.getName(),
				params.getDescription(), params.getPrice(), params.getPath(), params.getProductCategoryId()));

	}

	@PutMapping("/products/{productId}")
	public ProductDto editProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@Validated @RequestBody EditProductParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.editProduct(userId, params.getCompanyId(), productId,
				params.getName(), params.getDescription(), params.getPrice(), params.getNewPath(),
				params.getProductCategoryId()));

	}

	@PostMapping("/products/{productId}/block")
	public ProductDto blockProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@Validated @RequestBody StateProductParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.blockProduct(userId, params.getCompanyId(), productId));

	}

	@PostMapping("/products/{productId}/unlock")
	public ProductDto unlockProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@Validated @RequestBody StateProductParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.unlockProduct(userId, params.getCompanyId(), productId));

	}

	@GetMapping("/products/categories")
	public List<ProductCategoryDto> findAllProductCategories() {
		return toProductCategoryDtos(productManagementService.findAllProductCategories());
	}

	@GetMapping("/products/{companyId}")
	public List<ProductSummaryDto> findAllCompanyProducts(@PathVariable Long companyId) {
		return toProductSummaryDtos(productManagementService.findAllCompanyProducts(companyId));
	}

}
