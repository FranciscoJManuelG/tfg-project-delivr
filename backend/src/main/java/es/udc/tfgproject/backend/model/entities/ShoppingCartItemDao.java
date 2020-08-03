package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShoppingCartItemDao extends PagingAndSortingRepository<ShoppingCartItem, Long> {

	List<ShoppingCartItem> findByProductCompanyId(Long companyId);
}
