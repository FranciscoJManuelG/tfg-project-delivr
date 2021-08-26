package es.udc.tfgproject.backend.model.entities;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface WeeklyBalanceDao extends PagingAndSortingRepository<WeeklyBalance, Long> {

	Optional<WeeklyBalance> findByWeekNumberAndYearAndUserId(Integer weekNumber, Integer year, Long userId);

}
