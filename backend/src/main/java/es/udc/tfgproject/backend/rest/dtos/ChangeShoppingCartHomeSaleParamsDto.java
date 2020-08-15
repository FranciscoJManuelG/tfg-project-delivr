package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class ChangeShoppingCartHomeSaleParamsDto {

	private Long companyId;
	private Boolean homeSale;

	public ChangeShoppingCartHomeSaleParamsDto() {
	}

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@NotNull
	public Boolean getHomeSale() {
		return homeSale;
	}

	public void setHomeSale(Boolean homeSale) {
		this.homeSale = homeSale;
	}

}
