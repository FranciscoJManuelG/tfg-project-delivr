package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.ProductCategoryConversor.toProductCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.ProductConversor.toProductSummaryDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.services.ProductCatalogService;
import es.udc.tfgproject.backend.rest.dtos.ProductCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.ProductSummaryDto;

@RestController
@RequestMapping("/productCatalog")
public class ProductCatalogController {

	@Autowired
	private ProductCatalogService productCatalogService;

	@GetMapping("/products")
	public List<ProductSummaryDto> findProducts(@RequestParam Long companyId,
			@RequestParam(required = false) Long productCategoryId, @RequestParam(required = false) String keywords) {

		List<Product> productList = productCatalogService.findProducts(companyId, productCategoryId,
				keywords != null ? keywords.trim() : null);

		return toProductSummaryDtos(productList);

	}

	@GetMapping("/products/categories")
	public List<ProductCategoryDto> findCompanyProductCategories(@RequestParam Long companyId) {
		return toProductCategoryDtos(productCatalogService.findCompanyProductCategories(companyId));
	}

}
