package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class AddProductCategoryParamsDto {

	private String name;

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
