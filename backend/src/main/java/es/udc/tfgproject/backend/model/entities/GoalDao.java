package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoalDao extends PagingAndSortingRepository<Goal, Long> {

	Slice<Goal> findByCompanyIdOrderByIdDesc(Long companyId, PageRequest of);

	List<Goal> findByCompanyId(Long companyId);

}
