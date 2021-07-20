package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ReserveTable")
public class Reserve {

	public enum PeriodType {
		LUNCH, DINER
	};

	private Long id;
	private Set<ReserveItem> items = new HashSet<>();
	private User user;
	private Company company;
	private LocalDate date;
	private Integer diners;
	private PeriodType periodType;
	private EventEvaluation eventEvaluation;
	private BigDecimal totalPrice;
	private BigDecimal deposit;

	public Reserve() {
		super();
	}

	public Reserve(User user, Company company, LocalDate date, Integer diners, PeriodType periodType,
			BigDecimal totalPrice, BigDecimal deposit) {
		super();
		this.user = user;
		this.company = company;
		this.date = date;
		this.diners = diners;
		this.periodType = periodType;
		this.totalPrice = totalPrice;
		this.deposit = deposit;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "reserve")
	public Set<ReserveItem> getItems() {
		return items;
	}

	public void setItems(Set<ReserveItem> items) {
		this.items = items;
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
	@JoinColumn(name = "companyId")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getDiners() {
		return diners;
	}

	public void setDiners(Integer diners) {
		this.diners = diners;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	@OneToOne(mappedBy = "reserve", optional = false, fetch = FetchType.LAZY)
	public EventEvaluation getEventEvaluation() {
		return eventEvaluation;
	}

	public void setEventEvaluation(EventEvaluation eventEvaluation) {
		this.eventEvaluation = eventEvaluation;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	@Transient
	public Optional<ReserveItem> getItem(Long productId) {
		return items.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
	}

	public void addItem(ReserveItem item) {

		items.add(item);
		item.setReserve(this);

	}

}
