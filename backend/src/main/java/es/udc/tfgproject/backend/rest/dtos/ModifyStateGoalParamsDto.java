package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.NotNull;

public class ModifyStateGoalParamsDto {

	private Long companyId;
	private String option;

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@NotNull
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
