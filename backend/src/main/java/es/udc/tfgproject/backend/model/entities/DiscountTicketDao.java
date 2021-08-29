package es.udc.tfgproject.backend.model.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DiscountTicketDao extends PagingAndSortingRepository<DiscountTicket, Long> {

	Optional<DiscountTicket> findByCode(String code);

	@Query("SELECT d FROM DiscountTicket d WHERE d.user.id = ?1 AND d.used = false AND d.expirationDate >= ?2")
	Slice<DiscountTicket> findByUserIdWhereUsedIsFalseAndExpirationDateOrderByExpirationDateDesc(Long userId,
			LocalDateTime date, PageRequest of);

	List<DiscountTicket> findByGoalId(Long goalId);

}
