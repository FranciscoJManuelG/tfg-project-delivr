package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import es.udc.tfgproject.backend.model.services.Constantes;

@Entity
public class User {

	public enum RoleType {
		CLIENT, BUSINESSMAN, ADMIN
	};

	private Long id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private RoleType role;
	private ShoppingCart shoppingCart;
	private Menu menu;
	private BigDecimal globalBalance;
	private LocalDate renewDate;
	private Boolean feePaid;

	public User() {
	}

	public User(String userName, String password, String firstName, String lastName, String email, String phone) {

		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.globalBalance = new BigDecimal(0);
		this.renewDate = LocalDate.now().plusDays(Constantes.THREE_MONTHS_IN_DAYS);
		this.feePaid = false;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@OneToOne(mappedBy = "user", optional = false, fetch = FetchType.LAZY)
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	@OneToOne(mappedBy = "user", optional = false, fetch = FetchType.LAZY)
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public BigDecimal getGlobalBalance() {
		return globalBalance;
	}

	public void setGlobalBalance(BigDecimal globalBalance) {
		this.globalBalance = globalBalance;
	}

	public LocalDate getRenewDate() {
		return renewDate;
	}

	public void setRenewDate(LocalDate renewDate) {
		this.renewDate = renewDate;
	}

	public Boolean getFeePaid() {
		return feePaid;
	}

	public void setFeePaid(Boolean feePaid) {
		this.feePaid = feePaid;
	}

}
