package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductDao extends PagingAndSortingRepository<Product, Long>, CustomizedProductDao {

	List<Product> findByCompanyId(Long companyId);
}
