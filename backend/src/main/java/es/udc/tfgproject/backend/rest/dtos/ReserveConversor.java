package es.udc.tfgproject.backend.rest.dtos;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.ReserveItem;

public class ReserveConversor {

	private ReserveConversor() {

	}

	public final static List<ReserveSummaryDto> toReserveSummaryDtos(List<Reserve> reserves) {
		return reserves.stream().map(o -> toReserveSummaryDto(o)).collect(Collectors.toList());
	}

	public final static ReserveDto toReserveDto(Reserve reserve) {

		List<ReserveItemDto> items = reserve.getItems().stream().map(i -> toReserveItemDto(i))
				.collect(Collectors.toList());

		items.sort(Comparator.comparing(ReserveItemDto::getProductName));

		return new ReserveDto(reserve.getId(), items, toMillis(reserve.getDate()), reserve.getTotalPrice(),
				reserve.getDeposit(), reserve.getCompany().getName(), reserve.getUser().getFirstName(),
				reserve.getUser().getLastName(), reserve.getUser().getPhone(), reserve.getUser().getEmail(),
				reserve.getDiners(), reserve.getPeriodType().toString());

	}

	private final static ReserveSummaryDto toReserveSummaryDto(Reserve reserve) {

		return new ReserveSummaryDto(reserve.getId(), toMillis(reserve.getDate()), reserve.getDiners(),
				reserve.getPeriodType().toString(), reserve.getUser().getFirstName(), reserve.getUser().getLastName());

	}

	private final static ReserveItemDto toReserveItemDto(ReserveItem item) {

		return new ReserveItemDto(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
				item.getProductPrice(), item.getQuantity());

	}

	private final static long toMillis(LocalDate date) {
		return date.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
	}

}
