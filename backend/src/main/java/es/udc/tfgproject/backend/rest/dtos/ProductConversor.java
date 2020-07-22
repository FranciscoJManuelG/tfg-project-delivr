package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Product;

public class ProductConversor {

	private ProductConversor() {
	}

	public final static ProductDto toProductDto(Product product) {

		return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getBlock(), product.getImage().getPath(), product.getCompany().getId(),
				product.getProductCategory().getId());
	}

	public final static List<ProductSummaryDto> toProductSummaryDtos(List<Product> products) {
		return products.stream().map(o -> toProductSummaryDto(o)).collect(Collectors.toList());
	}

	private final static ProductSummaryDto toProductSummaryDto(Product product) {

		return new ProductSummaryDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getImage().getPath(), product.getProductCategory().getId());

	}

}
