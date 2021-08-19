package es.udc.tfgproject.backend.model.entities;

import java.time.LocalTime;

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
	private Integer capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private Integer reservePercentage;
	private LocalTime openingTime;
	private LocalTime closingTime;
	private LocalTime lunchTime;
	private LocalTime dinerTime; 
	private Boolean block;
	private User user;
	private CompanyCategory companyCategory;

	public Company() {
	}

	public Company(User user, String name, Integer capacity, Boolean reserve, Boolean homeSale, Integer reservePercentage,
			Boolean block, CompanyCategory companyCategory, LocalTime openingTime, LocalTime closingTime,
			LocalTime lunchTime, LocalTime dinerTime) {
		this.user = user;
		this.name = name;
		this.capacity = capacity;
		this.reserve = reserve;
		this.homeSale = homeSale;
		this.reservePercentage = reservePercentage;
		this.block = block;
		this.companyCategory = companyCategory;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.lunchTime = lunchTime;
		this.dinerTime = dinerTime;
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

	public LocalTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(LocalTime openingTime) {
		this.openingTime = openingTime;
	}

	public LocalTime getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(LocalTime closingTime) {
		this.closingTime = closingTime;
	}

	public LocalTime getLunchTime() {
		return lunchTime;
	}

	public void setLunchTime(LocalTime lunchTime) {
		this.lunchTime = lunchTime;
	}

	public LocalTime getDinerTime() {
		return dinerTime;
	}

	public void setDinerTime(LocalTime dinerTime) {
		this.dinerTime = dinerTime;
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

}
