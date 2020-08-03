package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShoppingCartDao extends PagingAndSortingRepository<ShoppingCart, Long> {
}
