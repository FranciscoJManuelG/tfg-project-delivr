package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "OrderTable")
public class Order {

	private Long id;
	private Set<OrderItem> items = new HashSet<>();
	private User user;
	private Company company;
	private LocalDateTime date;
	private Boolean homeSale;
	private String street;
	private String cp;
	private DiscountTicket discountTicket;
	private BigDecimal totalPrice;

	public Order() {
	}

	public Order(User user, Company company, LocalDateTime date, Boolean homeSale, String street, String cp,
			BigDecimal totalPrice) {

		this.user = user;
		this.company = company;
		this.date = date;
		this.homeSale = homeSale;
		this.street = street;
		this.cp = cp;
		this.totalPrice = totalPrice;

	}

	public Order(User user, Company company, LocalDateTime date, Boolean homeSale, String street, String cp,
			DiscountTicket discountTicket, BigDecimal totalPrice) {

		this.user = user;
		this.company = company;
		this.date = date;
		this.homeSale = homeSale;
		this.street = street;
		this.cp = cp;
		this.discountTicket = discountTicket;
		this.totalPrice = totalPrice;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "order")
	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Boolean getHomeSale() {
		return homeSale;
	}

	public void setHomeSale(Boolean homeSale) {
		this.homeSale = homeSale;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@OneToOne(mappedBy = "order", optional = false, fetch = FetchType.LAZY)
	public DiscountTicket getDiscountTicket() {
		return discountTicket;
	}

	public void setDiscountTicket(DiscountTicket discountTicket) {
		this.discountTicket = discountTicket;
	}

	@Transient
	public Optional<OrderItem> getItem(Long productId) {
		return items.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
	}

	public void addItem(OrderItem item) {

		items.add(item);
		item.setOrder(this);

	}

}
