package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Menu {

	private Long id;
	private Set<MenuItem> items = new HashSet<>();
	private User user;

	public Menu() {
	}

	public Menu(User user) {
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "menu")
	public Set<MenuItem> getItems() {
		return items;
	}

	public void setItems(Set<MenuItem> items) {
		this.items = items;
	}

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public Optional<MenuItem> getItem(Long productId) {
		return items.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
	}

	public void addItem(MenuItem item) {

		items.add(item);
		item.setMenu(this);

	}

	public void removeAll() {
		items = new HashSet<>();
	}

	public void removeItem(MenuItem existingMenuItem) {
		items.remove(existingMenuItem);
	}

	@Transient
	public int getTotalQuantity() {
		return items.stream().map(i -> i.getQuantity()).reduce(0, (a, b) -> a + b);
	}

	@Transient
	public BigDecimal getTotalPrice() {
		return items.stream().map(i -> i.getTotalPrice()).reduce(new BigDecimal(0), (a, b) -> a.add(b));
	}

	@Transient
	public boolean isEmpty() {
		return items.isEmpty();
	}

}
