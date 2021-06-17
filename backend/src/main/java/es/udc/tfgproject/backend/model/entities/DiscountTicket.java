package es.udc.tfgproject.backend.model.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DiscountTicket {

	public enum DiscountType {
		CASH, PERCENTAGE
	};

	private Long id;
	private String code;
	private LocalDateTime expirationDate;
	private DiscountType discountType;
	private User user;
	private Goal goal;
	private Order order;
	private Boolean used;

	public DiscountTicket() {
	}

	public DiscountTicket(String code, LocalDateTime expirationDate, DiscountType discountType, User user, Goal goal,
			Order order) {
		this.code = code;
		this.expirationDate = expirationDate;
		this.discountType = discountType;
		this.user = user;
		this.goal = goal;
		this.order = order;
		this.used = false;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "goalId")
	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

}
