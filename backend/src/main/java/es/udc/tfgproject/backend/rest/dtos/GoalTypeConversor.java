package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.GoalType;

public class GoalTypeConversor {

	private GoalTypeConversor() {
	}

	public final static GoalTypeDto toGoalTypeDto(GoalType goalType) {
		return new GoalTypeDto(goalType.getId(), goalType.getGoalName());
	}

	public final static List<GoalTypeDto> toGoalTypeDtos(List<GoalType> goalTypes) {
		return goalTypes.stream().map(c -> toGoalTypeDto(c)).collect(Collectors.toList());
	}

}
