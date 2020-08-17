package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.OrderConversor.toOrderDto;
import static es.udc.tfgproject.backend.rest.dtos.OrderConversor.toOrderSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.ShoppingCartConversor.toShoppingCartDto;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.ShoppingService;
import es.udc.tfgproject.backend.rest.common.ErrorsDto;
import es.udc.tfgproject.backend.rest.dtos.AddToShoppingCartParamsDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.BuyParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ChangeShoppingCartHomeSaleParamsDto;
import es.udc.tfgproject.backend.rest.dtos.IdDto;
import es.udc.tfgproject.backend.rest.dtos.OrderDto;
import es.udc.tfgproject.backend.rest.dtos.OrderSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.RemoveShoppingCartItemParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ShoppingCartDto;
import es.udc.tfgproject.backend.rest.dtos.UpdateShoppingCartItemQuantityParamsDto;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

	private final static String EMPTY_SHOPPING_CART_EXCEPTION_CODE = "project.exceptions.EmptyShoppingCartException";

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
			throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException {

		return new IdDto(shoppingService.buy(userId, shoppingCartId, params.getCompanyId(), params.getHomeSale(),
				params.getStreet(), params.getCp()).getId());

	}

	@GetMapping("/orders/{orderId}")
	public OrderDto findOrder(@RequestAttribute Long userId, @PathVariable Long orderId)
			throws InstanceNotFoundException, PermissionException {

		return toOrderDto(shoppingService.findOrder(userId, orderId));

	}

	@GetMapping("/userOrders")
	public BlockDto<OrderSummaryDto> findUserOrders(@RequestAttribute Long userId,
			@RequestParam(defaultValue = "0") int page) {

		Block<Order> orderBlock = shoppingService.findUserOrders(userId, page, 10);

		return new BlockDto<>(toOrderSummaryDtos(orderBlock.getItems()), orderBlock.getExistMoreItems());

	}

	@GetMapping("/companyOrders")
	public BlockDto<OrderSummaryDto> findCompanyOrders(@RequestAttribute Long userId, @RequestParam Long companyId,
			@RequestParam(defaultValue = "0") int page) throws InstanceNotFoundException, PermissionException {

		Block<Order> orderBlock = shoppingService.findCompanyOrders(userId, companyId, page, 10);

		return new BlockDto<>(toOrderSummaryDtos(orderBlock.getItems()), orderBlock.getExistMoreItems());

	}

}
