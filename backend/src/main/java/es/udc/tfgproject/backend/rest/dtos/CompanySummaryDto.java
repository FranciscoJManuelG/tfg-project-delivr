package es.udc.tfgproject.backend.rest.dtos;

public class CompanySummaryDto {

	private Long id;
	private String street;
	private String cp;
	private Long cityId;
	private String name;
	private int capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private int reservePercentage;
	private Long companyCategoryId;
	private String openingTime;
	private String closingTime;
	private String lunchTime;
	private String dinerTime;

	public CompanySummaryDto() {
	}

	public CompanySummaryDto(Long id, String street, String cp, Long cityId, String name, int capacity, Boolean reserve,
			Boolean homeSale, int reservePercentage, Long companyCategoryId, String openingTime,
			String closingTime, String lunchTime, String dinerTime) {
		this.id = id;
		this.street = street;
		this.cp = cp;
		this.cityId = cityId;
		this.name = name;
		this.capacity = capacity;
		this.reserve = reserve;
		this.homeSale = homeSale;
		this.reservePercentage = reservePercentage;
		this.companyCategoryId = companyCategoryId;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.lunchTime = lunchTime;
		this.dinerTime = dinerTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
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

	public Long getCompanyCategoryId() {
		return companyCategoryId;
	}

	public void setCompanyCategoryId(Long companyCategoryId) {
		this.companyCategoryId = companyCategoryId;
	}

	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

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
