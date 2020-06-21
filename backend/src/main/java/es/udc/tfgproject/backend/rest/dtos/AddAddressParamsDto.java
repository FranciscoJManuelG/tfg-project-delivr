package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddAddressParamsDto {

	private String street;
	private String cp;
	private Long cityId;

	@NotNull
	@Size(min = 1, max = 60)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@NotNull
	@Size(min = 1, max = 5)
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

}
