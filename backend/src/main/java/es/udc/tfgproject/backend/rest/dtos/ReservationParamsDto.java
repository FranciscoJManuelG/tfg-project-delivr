package es.udc.tfgproject.backend.rest.dtos;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservationParamsDto {

	private Long companyId;
	private LocalDate reservationDate;
	private Integer diners;
	private String periodType;

	@NotNull
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

	@NotNull
	@Min(value = 1)
	public Integer getDiners() {
		return diners;
	}

	public void setDiners(Integer diners) {
		this.diners = diners;
	}

	@NotNull
	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

}
