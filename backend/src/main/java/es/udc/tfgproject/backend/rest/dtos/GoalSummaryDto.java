package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class GoalSummaryDto {

	private Long id;
	private BigDecimal discountCash;
	private int discountPercentage;
	private Long goalTypeId;
	private Integer goalQuantity;
	private Boolean active;

	public GoalSummaryDto(Long id, BigDecimal discountCash, int discountPercentage, Long goalTypeId,
			Integer goalQuantity, Boolean active) {
		this.id = id;
		this.discountCash = discountCash;
		this.discountPercentage = discountPercentage;
		this.goalTypeId = goalTypeId;
		this.goalQuantity = goalQuantity;
		this.active = active;
	}

	public GoalSummaryDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
