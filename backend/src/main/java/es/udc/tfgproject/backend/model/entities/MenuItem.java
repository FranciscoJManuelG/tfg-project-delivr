package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class MenuItem {

	private Long id;
	private Product product;
	private Menu menu;
	private int quantity = 0;

	public MenuItem() {
	}

	public MenuItem(Product product, Menu menu, int quantity) {

		this.product = product;
		this.menu = menu;

		setQuantity(quantity);

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "menuId")
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {

		this.quantity = quantity;

	}

	public void incrementQuantity(int increment) {

		this.quantity += increment;

	}

	@Transient
	public BigDecimal getTotalPrice() {
		return product.getPrice().multiply(new BigDecimal(quantity));
	}

}
