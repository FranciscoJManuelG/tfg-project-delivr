package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {

	private Long id;
	private List<OrderItemDto> items;
	private long date;
	private BigDecimal totalPrice;
	private String street;
	private String cp;
	private String companyName;
	private Boolean homeSale;

	public OrderDto() {
	}

	public OrderDto(Long id, List<OrderItemDto> items, long date, BigDecimal totalPrice, String street, String cp,
			String companyName, Boolean homeSale) {

		this.id = id;
		this.items = items;
		this.date = date;
		this.totalPrice = totalPrice;
		this.street = street;
		this.cp = cp;
		this.companyName = companyName;
		this.homeSale = homeSale;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderItemDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Boolean getHomeSale() {
		return homeSale;
	}

	public void setHomeSale(Boolean homeSale) {
		this.homeSale = homeSale;
	}

}
