package es.udc.tfgproject.backend.model.entities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyAddressDao extends PagingAndSortingRepository<CompanyAddress, Long> {

	List<CompanyAddress> findByCompanyId(Long companyId);

	Optional<CompanyAddress> findByAddressId(Long addressId);

}
