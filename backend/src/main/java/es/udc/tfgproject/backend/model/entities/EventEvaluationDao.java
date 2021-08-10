package es.udc.tfgproject.backend.model.entities;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventEvaluationDao extends PagingAndSortingRepository<EventEvaluation, Long> {

	Optional<EventEvaluation> findByReserveId(Long reserveId);

	Slice<EventEvaluation> findByReserveCompanyIdAndDoneOrderByDateEvaluation(Long companyId, Boolean done,
			Pageable pageable);

	// @Query("select u.userName from User u inner join u.area ar where ar.idArea =
	// :idArea")

	@Query("SELECT e FROM EventEvaluation e WHERE e.reserve.user.id = ?1 AND e.done = false AND e.dateEvaluation < ?2")
	Slice<EventEvaluation> findByReserveUserIdAndDoneOrderByDateEvaluation(Long userId, LocalDate date,
			Pageable pageable);
}
