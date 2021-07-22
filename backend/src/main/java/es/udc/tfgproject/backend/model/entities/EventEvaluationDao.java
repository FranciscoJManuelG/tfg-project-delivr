package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventEvaluationDao extends PagingAndSortingRepository<EventEvaluation, Long> {

	Optional<EventEvaluation> findByReserveId(Long reserveId);

	Slice<EventEvaluation> findByReserveCompanyIdAndDoneOrderByDateEvaluation(Long companyId, Boolean done,
			Pageable pageable);

	Slice<EventEvaluation> findByReserveUserIdAndDoneOrderByDateEvaluation(Long userId, Boolean done,
			Pageable pageable);
}
