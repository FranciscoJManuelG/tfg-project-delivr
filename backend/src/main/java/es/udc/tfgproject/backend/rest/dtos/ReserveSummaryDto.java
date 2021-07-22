package es.udc.tfgproject.backend.rest.dtos;

public class ReserveSummaryDto {

	private Long id;
	private long date;
	private Integer diners;
	private String periodType;
	private String firstName;
	private String lastName;

	public ReserveSummaryDto() {
	}

	public ReserveSummaryDto(Long id, long date, Integer diners, String periodType, String firstName, String lastName) {
		this.id = id;
		this.date = date;
		this.diners = diners;
		this.periodType = periodType;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
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

}
