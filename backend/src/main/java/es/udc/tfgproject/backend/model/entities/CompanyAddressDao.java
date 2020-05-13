package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyAddressDao extends PagingAndSortingRepository<CompanyAddress, Long> {

	List<CompanyAddress> findByCompanyId(Long companyId);

}
