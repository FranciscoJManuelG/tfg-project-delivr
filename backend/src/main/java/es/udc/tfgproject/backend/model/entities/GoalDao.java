package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoalDao extends PagingAndSortingRepository<Goal, Long> {

	Slice<Goal> findByCompanyIdOrderByIdDesc(Long companyId, Pageable pageable);

	List<Goal> findByCompanyId(Long companyId);

}
