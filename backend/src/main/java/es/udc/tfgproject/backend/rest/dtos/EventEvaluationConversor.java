package es.udc.tfgproject.backend.rest.dtos;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.EventEvaluation;

public class EventEvaluationConversor {

	private EventEvaluationConversor() {
	}

	public final static List<CompanyEventEvaluationSummaryDto> toCompanyEventEvaluationSummaryDtos(
			List<EventEvaluation> eventEvaluations) {
		return eventEvaluations.stream().map(o -> toCompanyEventEvaluationSummaryDto(o)).collect(Collectors.toList());
	}

	private final static CompanyEventEvaluationSummaryDto toCompanyEventEvaluationSummaryDto(
			EventEvaluation eventEvaluation) {

		return new CompanyEventEvaluationSummaryDto(eventEvaluation.getPoints(), eventEvaluation.getOpinion(),
				eventEvaluation.getReserve().getUser().getUserName());

	}

	public final static List<UserEventEvaluationSummaryDto> toUserEventEvaluationSummaryDtos(
			List<EventEvaluation> eventEvaluations) {
		return eventEvaluations.stream().map(o -> toUserEventEvaluationSummaryDto(o)).collect(Collectors.toList());
	}

	private final static UserEventEvaluationSummaryDto toUserEventEvaluationSummaryDto(
			EventEvaluation eventEvaluation) {

		return new UserEventEvaluationSummaryDto(eventEvaluation.getId(),
				eventEvaluation.getReserve().getCompany().getName(), toMillis(eventEvaluation.getReserve().getDate()),
				eventEvaluation.getReserve().getPeriodType().toString());

	}

	private final static long toMillis(LocalDate date) {
		return date.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
	}

}
