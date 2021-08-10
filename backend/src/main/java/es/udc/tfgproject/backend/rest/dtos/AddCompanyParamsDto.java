package es.udc.tfgproject.backend.rest.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddCompanyParamsDto {

	private String name;
	private int capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private int reservePercentage;
	private Long companyCategoryId;
	private Integer reserveCapacity;
	private String openingTime;
	private String closingTime;
	private String lunchTime;
	private String dinerTime;

	@NotNull
	@Size(min = 1, max = 60)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Min(value = 1)
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@NotNull
	public Boolean getReserve() {
		return reserve;
	}

	public void setReserve(Boolean reserve) {
		this.reserve = reserve;
	}

	@NotNull
	public Boolean getHomeSale() {
		return homeSale;
	}

	public void setHomeSale(Boolean homeSale) {
		this.homeSale = homeSale;
	}

	@NotNull
	@Min(value = 0)
	public int getReservePercentage() {
		return reservePercentage;
	}

	public void setReservePercentage(int reservePercentage) {
		this.reservePercentage = reservePercentage;
	}

	@NotNull
	public Long getCompanyCategoryId() {
		return companyCategoryId;
	}

	public void setCompanyCategoryId(Long companyCategoryId) {
		this.companyCategoryId = companyCategoryId;
	}

	public Integer getReserveCapacity() {
		return reserveCapacity;
	}

	public void setReserveCapacity(Integer reserveCapacity) {
		this.reserveCapacity = reserveCapacity;
	}

	@NotNull
	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	@NotNull
	public String getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}

	public String getLunchTime() {
		return lunchTime;
	}

	public void setLunchTime(String lunchTime) {
		this.lunchTime = lunchTime;
	}

	public String getDinerTime() {
		return dinerTime;
	}

	public void setDinerTime(String dinerTime) {
		this.dinerTime = dinerTime;
	}

}
