package es.udc.tfgproject.backend.rest.dtos;

public class CityDto {

	private Long id;
	private String name;
	private String provinceName;

	public CityDto() {
	}

	public CityDto(Long id, String name, String provinceName) {

		this.id = id;
		this.name = name;
		this.provinceName = provinceName;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

}
