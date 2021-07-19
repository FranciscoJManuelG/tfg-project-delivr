package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReserveDao extends PagingAndSortingRepository<Reserve, Long> {

	Slice<Reserve> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);

	Slice<Reserve> findByCompanyIdOrderByDateDesc(Long companyId, Pageable pageable);

	List<Reserve> findByUserIdAndCompanyId(Long userId, Long companyId);

}
