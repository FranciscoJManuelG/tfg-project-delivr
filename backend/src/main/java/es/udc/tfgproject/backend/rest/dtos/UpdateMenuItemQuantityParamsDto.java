package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateMenuItemQuantityParamsDto {

	private Long productId;
	private Long companyId;
	private int quantity;

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

	@NotNull
	@Min(value = 1)
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
