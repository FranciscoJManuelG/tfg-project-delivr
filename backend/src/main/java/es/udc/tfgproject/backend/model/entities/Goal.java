package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Goal {

	private Long id;
	private BigDecimal discountCash;
	private int discountPercentage;
	private Company company;
	private GoalType goalType;
	private Integer goalQuantity;

	public Goal() {
	}

	public Goal(BigDecimal discountCash, int discountPercentage, Company company, GoalType goalType,
			Integer goalQuantity) {

		this.discountCash = discountCash;
		this.discountPercentage = discountPercentage;
		this.company = company;
		this.goalType = goalType;
		this.goalQuantity = goalQuantity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public Integer getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Integer discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "goalTypeId")
	public GoalType getGoalType() {
		return goalType;
	}

	public void setGoalType(GoalType goalType) {
		this.goalType = goalType;
	}

	public int getGoalQuantity() {
		return goalQuantity;
	}

	public void setGoalQuantity(int goalQuantity) {
		this.goalQuantity = goalQuantity;
	}

}
