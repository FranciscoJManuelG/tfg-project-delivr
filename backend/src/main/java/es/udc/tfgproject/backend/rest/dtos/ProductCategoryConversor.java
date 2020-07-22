package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.ProductCategory;

public class ProductCategoryConversor {

	private ProductCategoryConversor() {
	}

	public final static ProductCategoryDto toProductCategoryDto(ProductCategory category) {
		return new ProductCategoryDto(category.getId(), category.getName());
	}

	public final static List<ProductCategoryDto> toProductCategoryDtos(List<ProductCategory> categories) {
		return categories.stream().map(c -> toProductCategoryDto(c)).collect(Collectors.toList());
	}

}
