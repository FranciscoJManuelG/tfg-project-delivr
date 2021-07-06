package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.DiscountTicketConversor.toDiscountTicketSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.GoalConversor.toGoalDto;
import static es.udc.tfgproject.backend.rest.dtos.GoalConversor.toGoalSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.OrderConversor.toOrderDto;
import static es.udc.tfgproject.backend.rest.dtos.OrderConversor.toOrderSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.ShoppingCartConversor.toShoppingCartDto;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketHasExpiredException;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketUsedException;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectDiscountCodeException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.model.services.ShoppingService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.dtos.AddGoalParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddToShoppingCartParamsDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.BuyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ChangeShoppingCartHomeSaleParamsDto;
import es.udc.tfgproject.backend.rest.dtos.DiscountTicketSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.GoalDto;
import es.udc.tfgproject.backend.rest.dtos.GoalSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.IdDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyGoalParamsDto;
import es.udc.tfgproject.backend.rest.dtos.OrderDto;
import es.udc.tfgproject.backend.rest.dtos.OrderSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.PriceDto;
import es.udc.tfgproject.backend.rest.dtos.RedeemDiscountTicketParamsDto;
import es.udc.tfgproject.backend.rest.dtos.RemoveShoppingCartItemParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ShoppingCartDto;
import es.udc.tfgproject.backend.rest.dtos.UpdateShoppingCartItemQuantityParamsDto;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

	private final static String EMPTY_SHOPPING_CART_EXCEPTION_CODE = "project.exceptions.EmptyShoppingCartException";
	private final static String INCORRECT_DISCOUNT_CODE_EXCEPTION_CODE = "project.exceptions.IncorrectDiscountCodeException";
	private final static String DISCOUNT_TICKET_HAS_EXPIRED_EXCEPTION_CODE = "project.exceptions.DiscountTicketHasExpiredException";
	private final static String DISCOUNT_TICKET_USED_EXCEPTION_CODE = "project.exceptions.DiscountTicketUsedException";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ShoppingService shoppingService;

	@ExceptionHandler(EmptyShoppingCartException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleEmptyShoppingCartException(EmptyShoppingCartException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(EMPTY_SHOPPING_CART_EXCEPTION_CODE, null,
				EMPTY_SHOPPING_CART_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}
//	IncorrectDiscountCodeException, DiscountTicketHasExpiredException, DiscountTicketUsedException

	@ExceptionHandler(IncorrectDiscountCodeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleIncorrectDiscountCodeException(IncorrectDiscountCodeException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(INCORRECT_DISCOUNT_CODE_EXCEPTION_CODE, null,
				INCORRECT_DISCOUNT_CODE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(DiscountTicketHasExpiredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDiscountTicketHasExpiredException(DiscountTicketHasExpiredException exception,
			Locale locale) {

		String errorMessage = messageSource.getMessage(DISCOUNT_TICKET_HAS_EXPIRED_EXCEPTION_CODE, null,
				DISCOUNT_TICKET_HAS_EXPIRED_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(DiscountTicketUsedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDiscountTicketUsedException(DiscountTicketUsedException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(DISCOUNT_TICKET_USED_EXCEPTION_CODE, null,
				DISCOUNT_TICKET_USED_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/addToShoppingCart")
	public ShoppingCartDto addToShoppingCart(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@Validated @RequestBody AddToShoppingCartParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.addToShoppingCart(userId, shoppingCartId, params.getProductId(),
				params.getCompanyId(), params.getQuantity()));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/updateShoppingCartItemQuantity")
	public ShoppingCartDto updateShoppingCartItemQuantity(@RequestAttribute Long userId,
			@PathVariable Long shoppingCartId, @RequestBody UpdateShoppingCartItemQuantityParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.updateShoppingCartItemQuantity(userId, shoppingCartId,
				params.getProductId(), params.getCompanyId(), params.getQuantity()));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/removeShoppingCartItem")
	public ShoppingCartDto removeShoppingCartItem(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@RequestBody RemoveShoppingCartItemParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.removeShoppingCartItem(userId, shoppingCartId, params.getProductId(),
				params.getCompanyId()));

	}

	@CrossOrigin
	@GetMapping("/shoppingCarts/{shoppingCartId}")
	public ShoppingCartDto findShoppingCartProducts(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@RequestParam Long companyId) throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.findShoppingCartProducts(userId, shoppingCartId, companyId));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/changeShoppingCartHomeSale")
	public ShoppingCartDto changeShoppingCartHomeSale(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@RequestBody ChangeShoppingCartHomeSaleParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.changeShoppingCartHomeSale(userId, shoppingCartId,
				params.getCompanyId(), params.getHomeSale()));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/buy")
	public IdDto buy(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@Validated @RequestBody BuyParamsDto params)
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException,
			IncorrectDiscountCodeException, DiscountTicketHasExpiredException, DiscountTicketUsedException {

		return new IdDto(shoppingService.buy(userId, shoppingCartId, params.getCompanyId(), params.getHomeSale(),
				params.getStreet(), params.getCp(), params.getCodeDiscount()).getId());

	}

	@GetMapping("/orders/{orderId}")
	public OrderDto findOrder(@RequestAttribute Long userId, @PathVariable Long orderId)
			throws InstanceNotFoundException, PermissionException {

		return toOrderDto(shoppingService.findOrder(userId, orderId));

	}

	@GetMapping("/userOrders")
	public BlockDto<OrderSummaryDto> findUserOrders(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) {

		Block<Order> orderBlock = shoppingService.findUserOrders(userId, page, Constantes.PAGE);

		return new BlockDto<>(toOrderSummaryDtos(orderBlock.getItems()), orderBlock.getExistMoreItems());

	}

	@GetMapping("/companyOrders")
	public BlockDto<OrderSummaryDto> findCompanyOrders(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<Order> orderBlock = shoppingService.findCompanyOrders(userId, companyId, page, Constantes.PAGE);

		return new BlockDto<>(toOrderSummaryDtos(orderBlock.getItems()), orderBlock.getExistMoreItems());

	}

	@GetMapping("/userDiscountTickets")
	public BlockDto<DiscountTicketSummaryDto> findUserDiscountTickets(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<DiscountTicket> discountTicketBlock = shoppingService.findUserDiscountTicketsNotUsed(userId, page,
				Constantes.PAGE);

		return new BlockDto<>(toDiscountTicketSummaryDtos(discountTicketBlock.getItems()),
				discountTicketBlock.getExistMoreItems());

	}

	@GetMapping("/companyGoals")
	public BlockDto<GoalSummaryDto> findCompanyGoals(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<Goal> goalBlock = shoppingService.findCompanyGoals(userId, companyId, page, Constantes.PAGE);

		return new BlockDto<>(toGoalSummaryDtos(goalBlock.getItems()), goalBlock.getExistMoreItems());

	}

	@PostMapping("/goals")
	public GoalDto addGoal(@RequestAttribute Long userId, @Validated @RequestBody AddGoalParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toGoalDto(shoppingService.addGoal(userId, params.getCompanyId(), params.getDiscountType(),
				params.getDiscountCash(), params.getDiscountPercentage(), params.getGoalTypeId(),
				params.getGoalQuantity()));

	}

	@PutMapping("/goals/{goalId}")
	public GoalDto modifyGoal(@RequestAttribute Long userId, @Validated @RequestBody ModifyGoalParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toGoalDto(shoppingService.modifyGoal(userId, params.getCompanyId(), params.getGoalId(),
				params.getDiscountType(), params.getDiscountCash(), params.getDiscountPercentage(),
				params.getGoalTypeId(), params.getGoalQuantity()));

	}

	@DeleteMapping("/goals/{goalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeProduct(@RequestAttribute Long userId, @PathVariable Long goalId,
			@RequestParam(required = true) Long companyId) throws InstanceNotFoundException, PermissionException {

		shoppingService.removeGoal(userId, companyId, goalId);

	}

	@GetMapping("/redeemDiscountTicket")
	public PriceDto redeemDiscountTicket(@RequestAttribute Long userId,
			@Validated @RequestBody RedeemDiscountTicketParamsDto params)
			throws InstanceNotFoundException, PermissionException, IncorrectDiscountCodeException,
			EmptyShoppingCartException, DiscountTicketHasExpiredException, DiscountTicketUsedException {

		return new PriceDto(shoppingService.redeemDiscountTicket(userId, params.getCompanyId(),
				params.getShoppingCartId(), params.getCode()));

	}

}
