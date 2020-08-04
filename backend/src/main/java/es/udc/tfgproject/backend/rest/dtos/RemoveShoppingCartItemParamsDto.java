package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class RemoveShoppingCartItemParamsDto {

	private Long productId;
	private Long companyId;

	@NotNull
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
