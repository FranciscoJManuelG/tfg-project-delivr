package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.util.List;

public class MenuDto {

	private Long id;
	private List<MenuItemDto> items;
	private int totalQuantity;
	private BigDecimal totalPrice;
	private BigDecimal deposit;

	public MenuDto() {
	}

	public MenuDto(Long id, List<MenuItemDto> items, int totalQuantity, BigDecimal totalPrice) {

		this.id = id;
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

	public List<MenuItemDto> getItems() {
		return items;
	}

	public void setItems(List<MenuItemDto> items) {
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

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
}
