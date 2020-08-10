package es.udc.tfgproject.backend.rest.dtos;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderItem;

public class OrderConversor {

	private OrderConversor() {
	}

	public final static List<OrderSummaryDto> toOrderSummaryDtos(List<Order> orders) {
		return orders.stream().map(o -> toOrderSummaryDto(o)).collect(Collectors.toList());
	}

	public final static OrderDto toOrderDto(Order order) {

		List<OrderItemDto> items = order.getItems().stream().map(i -> toOrderItemDto(i)).collect(Collectors.toList());

		items.sort(Comparator.comparing(OrderItemDto::getProductName));

		return new OrderDto(order.getId(), items, toMillis(order.getDate()), order.getTotalPrice(), order.getStreet(),
				order.getCp(), order.getCompany().getName(), order.getHomeSale());

	}

	private final static OrderSummaryDto toOrderSummaryDto(Order order) {

		return new OrderSummaryDto(order.getId(), toMillis(order.getDate()));

	}

	private final static OrderItemDto toOrderItemDto(OrderItem item) {

		return new OrderItemDto(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
				item.getProductPrice(), item.getQuantity());

	}

	private final static long toMillis(LocalDateTime date) {
		return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
	}

}
