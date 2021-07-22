package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class ReserveItemDto {

	private Long id;
	private Long productId;
	private String productName;
	private BigDecimal productPrice;
	private int quantity;

	public ReserveItemDto() {
	}

	public ReserveItemDto(Long id, Long productId, String productName, BigDecimal productPrice, int quantity) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
