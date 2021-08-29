package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReserveItemDao extends PagingAndSortingRepository<ReserveItem, Long> {

	List<ReserveItem> findByReserveId(Long reserveId);

}
