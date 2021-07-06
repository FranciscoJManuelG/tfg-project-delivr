package es.udc.tfgproject.backend.rest.dtos;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.DiscountTicket;

public class DiscountTicketConversor {

	public final static List<DiscountTicketSummaryDto> toDiscountTicketSummaryDtos(
			List<DiscountTicket> discountTickets) {
		return discountTickets.stream().map(o -> toDiscountTicketsSummaryDto(o)).collect(Collectors.toList());
	}

	private final static DiscountTicketSummaryDto toDiscountTicketsSummaryDto(DiscountTicket discountTicket) {

		return new DiscountTicketSummaryDto(discountTicket.getCode(), toMillis(discountTicket.getExpirationDate()),
				discountTicket.getGoal().getDiscountCash(), discountTicket.getGoal().getDiscountPercentage(),
				discountTicket.getGoal().getCompany().getName());

	}

	private final static long toMillis(LocalDateTime date) {
		return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
	}

}
