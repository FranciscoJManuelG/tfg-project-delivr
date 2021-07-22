package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.EventEvaluation;

public class EventEvaluationConversor {

	private EventEvaluationConversor() {
	}

	public final static List<EventEvaluationSummaryDto> toEventEvaluationSummaryDtos(
			List<EventEvaluation> eventEvaluations) {
		return eventEvaluations.stream().map(o -> toEventEvaluationSummaryDto(o)).collect(Collectors.toList());
	}

	private final static EventEvaluationSummaryDto toEventEvaluationSummaryDto(EventEvaluation eventEvaluation) {

		return new EventEvaluationSummaryDto(eventEvaluation.getPoints(), eventEvaluation.getOpinion(),
				eventEvaluation.getReserve().getUser().getUserName());

	}

}
