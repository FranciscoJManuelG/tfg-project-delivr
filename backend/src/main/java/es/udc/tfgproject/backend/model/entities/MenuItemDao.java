package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuItemDao extends PagingAndSortingRepository<MenuItem, Long> {

	List<MenuItem> findByProductCompanyId(Long companyId);

}
