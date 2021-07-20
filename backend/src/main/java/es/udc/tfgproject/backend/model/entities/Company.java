package es.udc.tfgproject.backend.model.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Company {

	private Long id;
	private String name;
	private int capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private int reservePercentage;
	private Boolean block;
	private User user;
	private CompanyCategory companyCategory;
	private Integer reserveCapacity;

	public Company() {
	}

	public Company(User user, String name, int capacity, Boolean reserve, Boolean homeSale, int reservePercentage,
			Boolean block, CompanyCategory companyCategory, Integer reserveCapacity) {
		this.user = user;
		this.name = name;
		this.capacity = capacity;
		this.reserve = reserve;
		this.homeSale = homeSale;
		this.reservePercentage = reservePercentage;
		this.block = block;
		this.companyCategory = companyCategory;
		this.reserveCapacity = reserveCapacity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Boolean getReserve() {
		return reserve;
	}

	public void setReserve(Boolean reserve) {
		this.reserve = reserve;
	}

	public Boolean getHomeSale() {
		return homeSale;
	}

	public void setHomeSale(Boolean homeSale) {
		this.homeSale = homeSale;
	}

	public int getReservePercentage() {
		return reservePercentage;
	}

	public void setReservePercentage(int reservePercentage) {
		this.reservePercentage = reservePercentage;
	}

	public Boolean getBlock() {
		return block;
	}

	public void setBlock(Boolean block) {
		this.block = block;
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
	@JoinColumn(name = "companyCategoryId")
	public CompanyCategory getCompanyCategory() {
		return companyCategory;
	}

	public void setCompanyCategory(CompanyCategory companyCategory) {
		this.companyCategory = companyCategory;
	}

	public Integer getReserveCapacity() {
		return reserveCapacity;
	}

	public void setReserveCapacity(Integer reserveCapacity) {
		this.reserveCapacity = reserveCapacity;
	}

}
