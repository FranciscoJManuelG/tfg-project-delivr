package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class StateProductParamsDto {

	private Long companyId;

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
