package es.udc.tfgproject.backend.rest.dtos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;

public class ShoppingCartConversor {

	private ShoppingCartConversor() {
	}

	public final static ShoppingCartDto toShoppingCartDto(ShoppingCart cart) {

		List<ShoppingCartItemDto> items = cart.getItems().stream().map(item -> toShoppingCartItemDto(item))
				.collect(Collectors.toList());

		items.sort(Comparator.comparing(ShoppingCartItemDto::getProductName));

		return new ShoppingCartDto(cart.getId(), items, cart.getHomeSale(), cart.getTotalQuantity(),
				cart.getTotalPrice());

	}

	private final static ShoppingCartItemDto toShoppingCartItemDto(ShoppingCartItem item) {

		return new ShoppingCartItemDto(item.getProduct().getId(), item.getProduct().getName(),
				item.getProduct().getProductCategory().getId(), item.getProduct().getPrice(), item.getQuantity());

	}

}
