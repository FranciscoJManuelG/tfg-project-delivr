package es.udc.tfgproject.backend.rest.dtos;

import es.udc.tfgproject.backend.model.entities.Product;

public class ProductConversor {

	private ProductConversor() {
	}

	public final static ProductDto toProductDto(Product product) {

		return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getBlock(), product.getImage().getPath(), product.getCompany().getId(),
				product.getProductCategory().getId());
	}

}
