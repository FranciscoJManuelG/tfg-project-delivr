package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.util.List;

public class ReserveDto {

	private Long id;
	private List<ReserveItemDto> items;
	private long date;
	private BigDecimal totalPrice;
	private BigDecimal deposit;
	private String companyName;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private Integer diners;
	private String periodType;

	public ReserveDto() {
	}

	public ReserveDto(Long id, List<ReserveItemDto> items, long date, BigDecimal totalPrice, BigDecimal deposit,
			String companyName, String firstName, String lastName, String phone, String email, Integer diners,
			String periodType) {
		this.id = id;
		this.items = items;
		this.date = date;
		this.totalPrice = totalPrice;
		this.deposit = deposit;
		this.companyName = companyName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.diners = diners;
		this.periodType = periodType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ReserveItemDto> getItems() {
		return items;
	}

	public void setItems(List<ReserveItemDto> items) {
		this.items = items;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDiners() {
		return diners;
	}

	public void setDiners(Integer diners) {
		this.diners = diners;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

}
