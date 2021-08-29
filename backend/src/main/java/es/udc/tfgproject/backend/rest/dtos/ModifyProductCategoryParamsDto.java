package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ModifyProductCategoryParamsDto {

	private String name;

	@NotNull
	@Size(min = 1, max = 60)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
