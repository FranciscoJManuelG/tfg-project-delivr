package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyDao extends PagingAndSortingRepository<Company, Long> {

	Company findByUserId(Long userId);
}
