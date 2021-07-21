package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventEvaluationDao extends PagingAndSortingRepository<EventEvaluation, Long> {

	Optional<EventEvaluation> findByReserveId(Long reserveId);

	Slice<EventEvaluation> findByReserveCompanyIdOrderByDateEvaluation(Long companyId, Pageable pageable);

	Slice<EventEvaluation> findByReserveUserIdOrderByDateEvaluation(Long userId, Pageable pageable);
}
