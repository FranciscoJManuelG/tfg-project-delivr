package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class MenuItemDto {

	private Long productId;
	private String productName;
	private Long productCategoryId;
	private BigDecimal productPrice;
	private int quantity;

	public MenuItemDto() {
	}

	public MenuItemDto(Long productId, String productName, Long productCategoryId, BigDecimal productPrice,
			int quantity) {
		this.productId = productId;
		this.productName = productName;
		this.productCategoryId = productCategoryId;
		this.productPrice = productPrice;
		this.quantity = quantity;
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

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
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
