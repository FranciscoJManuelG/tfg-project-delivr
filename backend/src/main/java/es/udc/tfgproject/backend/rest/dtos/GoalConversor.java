package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Goal;

public class GoalConversor {

	private GoalConversor() {

	}

	public final static GoalDto toGoalDto(Goal goal) {

		return new GoalDto(goal.getId(), goal.getDiscountCash(), goal.getDiscountPercentage(),
				goal.getCompany().getId(), goal.getGoalType().getId(), goal.getGoalQuantity(), goal.getActive());
	}

	public final static List<GoalSummaryDto> toGoalSummaryDtos(List<Goal> goals) {
		return goals.stream().map(o -> toGoalSummaryDto(o)).collect(Collectors.toList());
	}

	private final static GoalSummaryDto toGoalSummaryDto(Goal goal) {

		return new GoalSummaryDto(goal.getId(), goal.getDiscountCash(), goal.getDiscountPercentage(),
				goal.getGoalType().getId(), goal.getGoalQuantity(), goal.getActive());

	}
}
