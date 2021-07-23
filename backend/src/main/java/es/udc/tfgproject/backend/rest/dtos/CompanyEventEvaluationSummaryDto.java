package es.udc.tfgproject.backend.rest.dtos;

public class CompanyEventEvaluationSummaryDto {

	private Integer points;
	private String opinion;
	private String username;

	public CompanyEventEvaluationSummaryDto() {
	}

	public CompanyEventEvaluationSummaryDto(Integer points, String opinion, String username) {
		this.points = points;
		this.opinion = opinion;
		this.username = username;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
