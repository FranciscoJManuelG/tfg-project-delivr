package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartDto {

	private Long id;
	private List<ShoppingCartItemDto> items;
	private int totalQuantity;
	private BigDecimal totalPrice;

	public ShoppingCartDto() {
	}

	public ShoppingCartDto(Long shoppingCartId, List<ShoppingCartItemDto> items, int totalQuantity,
			BigDecimal totalPrice) {

		this.id = shoppingCartId;
		this.items = items;
		this.totalQuantity = totalQuantity;
		this.totalPrice = totalPrice;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ShoppingCartItemDto> getItems() {
		return items;
	}

	public void setItems(List<ShoppingCartItemDto> items) {
		this.items = items;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
