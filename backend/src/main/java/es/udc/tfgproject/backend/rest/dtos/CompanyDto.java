package es.udc.tfgproject.backend.rest.dtos;

public class CompanyDto {

	private Long id;
	private String name;
	private int capacity;
	private Boolean reserve;
	private Boolean homeSale;
	private int reservePercentage;
	private String userName;
	private Long companyCategoryId;

	public CompanyDto() {
	}

	public CompanyDto(Long id, String name, int capacity, Boolean reserve, Boolean homeSale, int reservePercentage,
			Long companyCategoryId, String userName) {
		this.id = id;
		this.userName = userName;
		this.name = name;
		this.capacity = capacity;
		this.reserve = reserve;
		this.homeSale = homeSale;
		this.reservePercentage = reservePercentage;
		this.companyCategoryId = companyCategoryId;
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

}
