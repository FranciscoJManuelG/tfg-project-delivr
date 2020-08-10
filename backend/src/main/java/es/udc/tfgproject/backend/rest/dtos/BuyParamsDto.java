package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BuyParamsDto {

	private Long companyId;
	private Boolean homeSale;
	private String street;
	private String cp;
	private Long cityId;
	private Boolean saveAsFavAddress;

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

	@NotNull
	@Size(min = 1, max = 200)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@NotNull
	@Size(min = 1, max = 20)
	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	@NotNull
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@NotNull
	public Boolean getSaveAsFavAddress() {
		return saveAsFavAddress;
	}

	public void setSaveAsFavAddress(Boolean saveAsFavAddress) {
		this.saveAsFavAddress = saveAsFavAddress;
	}

}
