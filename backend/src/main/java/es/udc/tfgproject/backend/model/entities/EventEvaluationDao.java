package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventEvaluationDao extends PagingAndSortingRepository<EventEvaluation, Long> {

	Optional<EventEvaluation> findByReserveId(Long reserveId);

}
