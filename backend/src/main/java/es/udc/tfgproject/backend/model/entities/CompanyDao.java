package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyDao extends PagingAndSortingRepository<Company, Long> {

	Optional<Company> findByUserId(Long userId);
}
