package es.udc.tfgproject.backend.rest.dtos;

public class FavouriteAddressDto {

	private Long id;
	private String street;
	private String cp;
	private Long cityId;

	public FavouriteAddressDto() {
	}

	public FavouriteAddressDto(Long id, String street, String cp, Long cityId) {
		this.id = id;
		this.street = street;
		this.cp = cp;
		this.cityId = cityId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

}
