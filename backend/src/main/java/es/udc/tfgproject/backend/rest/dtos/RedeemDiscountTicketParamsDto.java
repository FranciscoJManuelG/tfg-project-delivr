package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RedeemDiscountTicketParamsDto {

	private Long companyId;
	private Long shoppingCartId;
	private String code;

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@NotNull
	public Long getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(Long shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	@NotNull
	@Size(min = 10, max = 10)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
