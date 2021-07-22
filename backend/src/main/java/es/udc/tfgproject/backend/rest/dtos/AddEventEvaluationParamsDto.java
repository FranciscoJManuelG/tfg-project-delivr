package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddEventEvaluationParamsDto {

	private Integer points;
	private String opinion;

	@NotNull
	@Min(value = 1)
	@Max(value = 5)
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@NotNull
	@Size(min = 1, max = 250)
	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

}
