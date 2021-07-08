package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GoalDao extends PagingAndSortingRepository<Goal, Long> {

	@Query("SELECT g FROM Goal g WHERE g.company.id = ?1")
	Slice<Goal> findByCompanyIdOrderByIdDesc(Long companyId, PageRequest of);

	List<Goal> findByCompanyId(Long companyId);

}
