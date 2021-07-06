package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class GoalSummaryDto {

	private BigDecimal discountCash;
	private int discountPercentage;
	private Long goalTypeId;
	private Integer goalQuantity;

	public GoalSummaryDto(BigDecimal discountCash, int discountPercentage, Long goalTypeId, Integer goalQuantity) {
		this.discountCash = discountCash;
		this.discountPercentage = discountPercentage;
		this.goalTypeId = goalTypeId;
		this.goalQuantity = goalQuantity;
	}

	public GoalSummaryDto() {
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
