package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDao extends PagingAndSortingRepository<Order, Long> {

	Slice<Order> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);

	Slice<Order> findByCompanyIdOrderByDateDesc(Long companyId, Pageable pageable);

	List<Order> findByUserIdAndCompanyId(Long userId, Long companyId);

}
