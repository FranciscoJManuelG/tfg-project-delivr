package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressDao extends PagingAndSortingRepository<Address, Long> {

	// Slice<Address> findByCompanyAddressCompanyId(Long companyId, Pageable
	// pageable);

}
