package es.udc.tfgproject.backend.rest.dtos;

public class GoalTypeDto {

	private Long id;
	private String goalName;

	public GoalTypeDto() {
	}

	public GoalTypeDto(Long id, String goalName) {

		this.id = id;
		this.goalName = goalName;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

}
