package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class RemoveShoppingCartItemParamsDto {

	private Long productId;

	@NotNull
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
