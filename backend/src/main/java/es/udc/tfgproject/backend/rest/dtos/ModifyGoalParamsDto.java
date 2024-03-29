package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ModifyGoalParamsDto {

	private Long companyId;
	private String discountType;
	private BigDecimal discountCash;
	private Integer discountPercentage;
	private Long goalTypeId;
	private int goalQuantity;

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@NotNull
	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	@NotNull
	@Min(value = 0)
	public BigDecimal getDiscountCash() {
		return discountCash;
	}

	public void setDiscountCash(BigDecimal discountCash) {
		this.discountCash = discountCash;
	}

	@NotNull
	public Integer getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Integer discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@NotNull
	public Long getGoalTypeId() {
		return goalTypeId;
	}

	public void setGoalTypeId(Long goalTypeId) {
		this.goalTypeId = goalTypeId;
	}

	@NotNull
	@Min(value = 1)
	public int getGoalQuantity() {
		return goalQuantity;
	}

	public void setGoalQuantity(int goalQuantity) {
		this.goalQuantity = goalQuantity;
	}

}
