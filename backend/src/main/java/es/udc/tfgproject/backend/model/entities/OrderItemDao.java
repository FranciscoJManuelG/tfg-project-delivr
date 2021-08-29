package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderItemDao extends PagingAndSortingRepository<OrderItem, Long> {

	List<OrderItem> findByOrderId(Long orderId);

}
