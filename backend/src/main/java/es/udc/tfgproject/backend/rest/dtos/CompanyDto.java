package es.udc.tfgproject.backend.rest.dtos;

public class CompanyDto {

	private Long id;
	private String name;
	private Integer capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private Integer reservePercentage;
	private Boolean block;
	private String userName;
	private Long companyCategoryId;
	private String openingTime;
	private String closingTime;
	private String lunchTime;
	private String dinerTime;

	public CompanyDto() {
	}

	public CompanyDto(Long id, String name, Integer capacity, Boolean reserve, Boolean homeSale,
			Integer reservePercentage, Boolean block, String userName, Long companyCategoryId, String openingTime,
			String closingTime, String lunchTime, String dinerTime) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
		this.reserve = reserve;
		this.homeSale = homeSale;
		this.reservePercentage = reservePercentage;
		this.block = block;
		this.userName = userName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
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

	public Integer getReservePercentage() {
		return reservePercentage;
	}

	public void setReservePercentage(Integer reservePercentage) {
		this.reservePercentage = reservePercentage;
	}

	public Boolean getBlock() {
		return block;
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
