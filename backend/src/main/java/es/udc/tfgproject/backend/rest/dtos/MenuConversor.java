package es.udc.tfgproject.backend.rest.dtos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuItem;

public class MenuConversor {

	private MenuConversor() {

	}

	public final static MenuDto toMenuDto(Menu menu) {

		List<MenuItemDto> items = menu.getItems().stream().map(item -> toMenuItemDto(item))
				.collect(Collectors.toList());

		items.sort(Comparator.comparing(MenuItemDto::getProductName));

		return new MenuDto(menu.getId(), items, menu.getTotalQuantity(), menu.getTotalPrice());

	}

	private final static MenuItemDto toMenuItemDto(MenuItem item) {

		return new MenuItemDto(item.getProduct().getId(), item.getProduct().getName(),
				item.getProduct().getProductCategory().getId(), item.getProduct().getPrice(), item.getQuantity());

	}

}
