package es.udc.tfgproject.backend.model.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class EventEvaluation {

	private Long id;
	private Reserve reserve;
	private Integer points;
	private String opinion;
	private Boolean done;
	private LocalDate dateEvaluation;

	public EventEvaluation() {
	}

	public EventEvaluation(Reserve reserve, LocalDate dateEvaluation) {
		this.reserve = reserve;
		this.dateEvaluation = dateEvaluation;
		this.done = false;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "reserveId")
	public Reserve getReserve() {
		return reserve;
	}

	public void setReserve(Reserve reserve) {
		this.reserve = reserve;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public LocalDate getDateEvaluation() {
		return dateEvaluation;
	}

	public void setDateEvaluation(LocalDate dateEvaluation) {
		this.dateEvaluation = dateEvaluation;
	}

}
