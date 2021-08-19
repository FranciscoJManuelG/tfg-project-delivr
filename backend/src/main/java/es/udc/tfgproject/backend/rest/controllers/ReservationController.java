package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.EventEvaluationConversor.toCompanyEventEvaluationSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.EventEvaluationConversor.toUserEventEvaluationSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.MenuConversor.toMenuDto;
import static es.udc.tfgproject.backend.rest.dtos.ReserveConversor.toReserveDto;
import static es.udc.tfgproject.backend.rest.dtos.ReserveConversor.toReserveSummaryDtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.Reserve.PeriodType;
import es.udc.tfgproject.backend.model.exceptions.CompanyDoesntAllowReservesException;
import es.udc.tfgproject.backend.model.exceptions.EmptyMenuException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.MaximumCapacityExceededException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.exceptions.ReservationDateIsBeforeNowException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.model.services.ReservationService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.dtos.AddEventEvaluationParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddToMenuParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AllowDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CompanyEventEvaluationSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.DinersDto;
import es.udc.tfgproject.backend.rest.dtos.IdDto;
import es.udc.tfgproject.backend.rest.dtos.MenuDto;
import es.udc.tfgproject.backend.rest.dtos.PriceDto;
import es.udc.tfgproject.backend.rest.dtos.RemoveMenuItemParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ReservationParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ReserveDto;
import es.udc.tfgproject.backend.rest.dtos.ReserveSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.UpdateMenuItemQuantityParamsDto;
import es.udc.tfgproject.backend.rest.dtos.UserEventEvaluationSummaryDto;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	private final static String EMPTY_MENU_EXCEPTION_CODE = "project.exceptions.EmptyMenuException";
	private final static String MAXIMUM_CAPACITY_EXCEEDED_EXCEPTION = "project.exceptions.MaximumCapacityExceededException";
	private final static String RESERVATION_DATE_IS_BEFORE_NOW_EXCEPTION = "project.exceptions.ReservationDateIsBeforeNowException";
	private final static String COMPANY_DOESNT_ALLOW_RESERVES = "project.exceptions.CompanyDoesntAllowReservesException";
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ReservationService reservationService;

	@ExceptionHandler(EmptyMenuException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleEmptyMenuException(EmptyMenuException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(EMPTY_MENU_EXCEPTION_CODE, null, EMPTY_MENU_EXCEPTION_CODE,
				locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(MaximumCapacityExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMaximumCapacityExceededException(MaximumCapacityExceededException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(MAXIMUM_CAPACITY_EXCEEDED_EXCEPTION, null,
				MAXIMUM_CAPACITY_EXCEEDED_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(ReservationDateIsBeforeNowException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleReservationDateIsBeforeNowException(ReservationDateIsBeforeNowException exception,
			Locale locale) {

		String errorMessage = messageSource.getMessage(RESERVATION_DATE_IS_BEFORE_NOW_EXCEPTION, null,
				RESERVATION_DATE_IS_BEFORE_NOW_EXCEPTION, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(CompanyDoesntAllowReservesException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleCompanyDoesntAllowReservesException(CompanyDoesntAllowReservesException exception,
			Locale locale) {

		String errorMessage = messageSource.getMessage(COMPANY_DOESNT_ALLOW_RESERVES, null,
				COMPANY_DOESNT_ALLOW_RESERVES, locale);

		return new ErrorsDto(errorMessage);

	}

	@PostMapping("/menus/{menuId}/addToMenu")
	public MenuDto addToMenu(@RequestAttribute Long userId, @PathVariable Long menuId,
			@Validated @RequestBody AddToMenuParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toMenuDto(reservationService.addToMenu(userId, menuId, params.getProductId(), params.getCompanyId(),
				params.getQuantity()));

	}

	@PostMapping("/menus/{menuId}/updateMenuItemQuantity")
	public MenuDto updateMenuItemQuantity(@RequestAttribute Long userId, @PathVariable Long menuId,
			@RequestBody UpdateMenuItemQuantityParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toMenuDto(reservationService.updateMenuItemQuantity(userId, menuId, params.getProductId(),
				params.getCompanyId(), params.getQuantity()));

	}

	@PostMapping("/menus/{menuId}/removeMenuItem")
	public MenuDto removeMenuItem(@RequestAttribute Long userId, @PathVariable Long menuId,
			@RequestBody RemoveMenuItemParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toMenuDto(
				reservationService.removeMenuItem(userId, menuId, params.getProductId(), params.getCompanyId()));

	}

	@CrossOrigin
	@GetMapping("/menus/{menuId}")
	public MenuDto findMenuProducts(@RequestAttribute Long userId, @PathVariable Long menuId,
			@RequestParam Long companyId) throws InstanceNotFoundException, PermissionException {

		return toMenuDto(reservationService.findMenuProducts(userId, menuId, companyId));

	}

	@PostMapping("/menus/{menuId}/reservation")
	public IdDto reservation(@RequestAttribute Long userId, @PathVariable Long menuId,
			@Validated @RequestBody ReservationParamsDto params)
			throws InstanceNotFoundException, PermissionException, EmptyMenuException, MaximumCapacityExceededException,
			ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {

		LocalDate reservationDate = LocalDate.parse(params.getReservationDate().trim());
		return new IdDto(reservationService.reservation(userId, menuId, params.getCompanyId(), reservationDate,
				params.getDiners(), PeriodType.valueOf(params.getPeriodType())).getId());
	}

	@PostMapping("/reserves/{reserveId}/cancelReservation")
	public void cancelReservation(@RequestAttribute Long userId, @PathVariable Long reserveId)
			throws InstanceNotFoundException, PermissionException {

		reservationService.cancelReservation(userId, reserveId);

	}

	@DeleteMapping("/reserves/{reserveId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeReservation(@RequestAttribute Long userId, @PathVariable Long reserveId)
			throws InstanceNotFoundException, PermissionException {

		reservationService.removeReservation(userId, reserveId);

	}

	@GetMapping("/menus/calculateDeposit")
	public PriceDto calculateDepositFromPercentage(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam BigDecimal totalPrice) throws InstanceNotFoundException {

		return new PriceDto(reservationService.calculateDepositFromPercentage(companyId, totalPrice));

	}

	@GetMapping("/menus/checkCapacity")
	public AllowDto checkCapacity(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam String reservationDate, @RequestParam String periodType, @RequestParam Integer diners)
			throws InstanceNotFoundException, PermissionException, MaximumCapacityExceededException,
			ReservationDateIsBeforeNowException {

		LocalDate date = LocalDate.parse(reservationDate.trim());
		return new AllowDto(reservationService.checkCapacity(companyId, date, PeriodType.valueOf(periodType), diners));

	}

	@GetMapping("/menus/obtainMaxDinersAllowed")
	public DinersDto obtainMaxDinersAllowed(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam String reservationDate, @RequestParam String periodType)
			throws InstanceNotFoundException, PermissionException {

		LocalDate date = LocalDate.parse(reservationDate.trim());
		return new DinersDto(
				reservationService.obtainMaxDinersAllowed(companyId, date, PeriodType.valueOf(periodType)));

	}

	@GetMapping("/reserves/{reserveId}")
	public ReserveDto findReserve(@RequestAttribute Long userId, @PathVariable Long reserveId)
			throws InstanceNotFoundException, PermissionException {

		return toReserveDto(reservationService.findReserve(userId, reserveId));

	}

	@GetMapping("/userReserves")
	public BlockDto<ReserveSummaryDto> findUserReserves(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) {

		Block<Reserve> reserveBlock = reservationService.findUserReserves(userId, page, Constantes.SIZE);

		return new BlockDto<>(toReserveSummaryDtos(reserveBlock.getItems()), reserveBlock.getExistMoreItems());

	}

	@GetMapping("/companyReserves")
	public BlockDto<ReserveSummaryDto> findCompanyReserves(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam String reservationDate, @RequestParam String periodType,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		LocalDate date = LocalDate.parse(reservationDate.trim());
		Block<Reserve> reserveBlock = reservationService.findCompanyReserves(userId, companyId, date,
				PeriodType.valueOf(periodType), page, Constantes.SIZE);

		return new BlockDto<>(toReserveSummaryDtos(reserveBlock.getItems()), reserveBlock.getExistMoreItems());

	}

	@GetMapping("/companyReservesCanceled")
	public BlockDto<ReserveSummaryDto> companyReservesCanceled(@RequestAttribute Long userId,
			@RequestParam Long companyId, @RequestParam(defaultValue = "0") int page)
			throws InstanceNotFoundException, PermissionException {

		Block<Reserve> reserveBlock = reservationService.findCompanyReservesCanceled(userId, companyId, page,
				Constantes.SIZE);

		return new BlockDto<>(toReserveSummaryDtos(reserveBlock.getItems()), reserveBlock.getExistMoreItems());

	}

	@PostMapping("/eventEvaluation/{eventEvaluationId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addEventEvaluation(@RequestAttribute Long userId, @PathVariable Long eventEvaluationId,
			@Validated @RequestBody AddEventEvaluationParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		reservationService.addEventEvaluation(userId, eventEvaluationId, params.getPoints(), params.getOpinion());

	}

	@GetMapping("/userEventEvaluations")
	public BlockDto<UserEventEvaluationSummaryDto> findUserEventEvaluations(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<EventEvaluation> eventEvaluationBlock = reservationService.findUserEventEvaluations(userId, page,
				Constantes.SIZE);

		return new BlockDto<>(toUserEventEvaluationSummaryDtos(eventEvaluationBlock.getItems()),
				eventEvaluationBlock.getExistMoreItems());

	}

	@GetMapping("/companyEventEvaluations")
	public BlockDto<CompanyEventEvaluationSummaryDto> findCompanyEventEvaluations(@RequestAttribute Long userId,
			@RequestParam Long companyId, @RequestParam(defaultValue = "0") int page)
			throws InstanceNotFoundException, PermissionException {

		Block<EventEvaluation> eventEvaluationBlock = reservationService.findCompanyEventEvaluations(userId, companyId,
				page, Constantes.SIZE);

		return new BlockDto<>(toCompanyEventEvaluationSummaryDtos(eventEvaluationBlock.getItems()),
				eventEvaluationBlock.getExistMoreItems());

	}

}
