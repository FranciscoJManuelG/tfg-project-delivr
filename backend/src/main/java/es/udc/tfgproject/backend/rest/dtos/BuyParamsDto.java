package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BuyParamsDto {

	private Long companyId;
	private Boolean homeSale;
	private String street;
	private String cp;
	private String codeDiscount;

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

	@Size(min = 1, max = 200)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Size(min = 1, max = 20)
	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCodeDiscount() {
		return codeDiscount;
	}

	public void setCodeDiscount(String codeDiscount) {
		this.codeDiscount = codeDiscount;
	}

}
