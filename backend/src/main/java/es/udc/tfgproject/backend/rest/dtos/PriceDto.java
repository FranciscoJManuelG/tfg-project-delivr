package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class PriceDto {

	private BigDecimal price;

	public PriceDto() {
	}

	public PriceDto(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
