package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DiscountTicketDao extends PagingAndSortingRepository<DiscountTicket, Long> {

	Optional<DiscountTicket> findByCode(String code);

}