package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.Reserve.PeriodType;
import es.udc.tfgproject.backend.model.exceptions.CompanyDoesntAllowReservesException;
import es.udc.tfgproject.backend.model.exceptions.EmptyMenuException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.MaximumCapacityExceededException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.exceptions.ReservationDateIsBeforeNowException;

public interface ReservationService {

	Menu addToMenu(Long userId, Long menuId, Long productId, Long companyId, int quantity)
			throws InstanceNotFoundException, PermissionException;

	Menu updateMenuItemQuantity(Long userId, Long menuId, Long productId, Long companyId, int quantity)
			throws InstanceNotFoundException, PermissionException;

	Menu removeMenuItem(Long userId, Long menuId, Long productId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

	Menu findMenuProducts(Long userId, Long menuId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

	Reserve reservation(Long userId, Long menuId, Long companyId, LocalDate reservationDate, Integer diners,
			PeriodType periodType) throws InstanceNotFoundException, PermissionException, EmptyMenuException,
			MaximumCapacityExceededException, ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException;

	void cancelReservation(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException;

	void removeReservation(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException;

	BigDecimal calculateDepositFromPercentage(Long companyId, BigDecimal totalPrice) throws InstanceNotFoundException;

	Boolean checkCapacity(Long companyId, LocalDate reservationDate, PeriodType periodType, Integer diners)
			throws MaximumCapacityExceededException, InstanceNotFoundException, ReservationDateIsBeforeNowException;

	Integer obtainMaxDinersAllowed(Long companyId, LocalDate reservationDate, PeriodType periodType)
			throws InstanceNotFoundException;

	Reserve findReserve(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException;

	Block<Reserve> findUserReserves(Long userId, int page, int size);

	Block<Reserve> findCompanyReserves(Long userId, Long companyId, LocalDate date, PeriodType periodType, int page,
			int size) throws InstanceNotFoundException, PermissionException;

	Block<Reserve> findCompanyReservesCanceled(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

	void addEventEvaluation(Long userId, Long eventEvaluationId, Integer points, String opinion)
			throws PermissionException, InstanceNotFoundException;

	Block<EventEvaluation> findUserEventEvaluations(Long userId, int page, int size)
			throws PermissionException, InstanceNotFoundException;

	Block<EventEvaluation> findCompanyEventEvaluations(Long userId, Long companyId, int page, int size)
			throws PermissionException, InstanceNotFoundException;

}
