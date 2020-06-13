package es.udc.tfgproject.backend.rest.dtos;

public class CompanyCategoryDto {

	private Long id;
	private String name;

	public CompanyCategoryDto() {
	}

	public CompanyCategoryDto(Long id, String name) {

		this.id = id;
		this.name = name;

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

}
