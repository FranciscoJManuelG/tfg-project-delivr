package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class DiscountTicketSummaryDto {

	private String code;
	private Long expirationDate;
	private BigDecimal discountCash;
	private Integer discountPercentage;
	private String companyName;

	public DiscountTicketSummaryDto() {
	}

	public DiscountTicketSummaryDto(String code, Long expirationDate, BigDecimal discountCash,
			Integer discountPercentage, String companyName) {
		this.code = code;
		this.expirationDate = expirationDate;
		this.discountCash = discountCash;
		this.discountPercentage = discountPercentage;
		this.companyName = companyName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Long expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getDiscountCash() {
		return discountCash;
	}

	public void setDiscountCash(BigDecimal discountCash) {
		this.discountCash = discountCash;
	}

	public Integer getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Integer discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
