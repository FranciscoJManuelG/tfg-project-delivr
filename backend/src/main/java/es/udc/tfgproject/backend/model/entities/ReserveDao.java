package es.udc.tfgproject.backend.model.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.udc.tfgproject.backend.model.entities.Reserve.PeriodType;

public interface ReserveDao extends PagingAndSortingRepository<Reserve, Long> {

	Slice<Reserve> findByUserIdOrderByDateAsc(Long userId, Pageable pageable);

	Slice<Reserve> findByCompanyIdAndDateAndPeriodTypeOrderByDateDesc(Long companyId, LocalDate date,
			PeriodType periodType, Pageable pageable);

	Slice<Reserve> findByCompanyIdOrderByDateDesc(Long companyId, Pageable pageable);

	List<Reserve> findByUserIdAndCompanyId(Long userId, Long companyId);

	List<Reserve> findByCompanyId(Long companyId);

	@Query("SELECT COALESCE(SUM(r.diners), 0) FROM Reserve r WHERE r.date = ?1 AND r.periodType = ?2")
	Integer getSumDinersByReservationDateAndPeriodType(LocalDate date, PeriodType periodType);

}
