package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class GoalDto {

	private BigDecimal discountCash;
	private int discountPercentage;
	private Long companyId;
	private Long goalTypeId;
	private Integer goalQuantity;

	public GoalDto() {

	}

	public GoalDto(BigDecimal discountCash, int discountPercentage, Long companyId, Long goalTypeId,
			Integer goalQuantity) {
		this.discountCash = discountCash;
		this.discountPercentage = discountPercentage;
		this.companyId = companyId;
		this.goalTypeId = goalTypeId;
		this.goalQuantity = goalQuantity;
	}

	public BigDecimal getDiscountCash() {
		return discountCash;
	}

	public void setDiscountCash(BigDecimal discountCash) {
		this.discountCash = discountCash;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getGoalTypeId() {
		return goalTypeId;
	}

	public void setGoalTypeId(Long goalTypeId) {
		this.goalTypeId = goalTypeId;
	}

	public Integer getGoalQuantity() {
		return goalQuantity;
	}

	public void setGoalQuantity(Integer goalQuantity) {
		this.goalQuantity = goalQuantity;
	}

}
