package es.udc.tfgproject.backend.rest.dtos;

public class UserEventEvaluationSummaryDto {

	private Long id;
	private String companyName;
	private Long reservationDate;
	private String periodType;

	public UserEventEvaluationSummaryDto() {
	}

	public UserEventEvaluationSummaryDto(Long id, String companyName, Long reservationDate, String periodType) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.reservationDate = reservationDate;
		this.periodType = periodType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Long reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

}
