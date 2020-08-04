package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.ShoppingCartConversor.toShoppingCartDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.ShoppingService;
import es.udc.tfgproject.backend.rest.dtos.AddToShoppingCartParamsDto;
import es.udc.tfgproject.backend.rest.dtos.RemoveShoppingCartItemParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ShoppingCartDto;
import es.udc.tfgproject.backend.rest.dtos.UpdateShoppingCartItemQuantityParamsDto;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

	@Autowired
	private ShoppingService shoppingService;

	@PostMapping("/shoppingCarts/{shoppingCartId}/addToShoppingCart")
	public ShoppingCartDto addToShoppingCart(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@Validated @RequestBody AddToShoppingCartParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(
				shoppingService.addToShoppingCart(userId, shoppingCartId, params.getProductId(), params.getQuantity()));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/updateShoppingCartItemQuantity")
	public ShoppingCartDto updateShoppingCartItemQuantity(@RequestAttribute Long userId,
			@PathVariable Long shoppingCartId, @RequestBody UpdateShoppingCartItemQuantityParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.updateShoppingCartItemQuantity(userId, shoppingCartId,
				params.getProductId(), params.getQuantity()));

	}

	@PostMapping("/shoppingCarts/{shoppingCartId}/removeShoppingCartItem")
	public ShoppingCartDto removeShoppingCartItem(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@RequestBody RemoveShoppingCartItemParamsDto params) throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.removeShoppingCartItem(userId, shoppingCartId, params.getProductId()));

	}

	@GetMapping("/shoppingCarts/{shoppingCartId}")
	public ShoppingCartDto findShoppingCartProducts(@RequestAttribute Long userId, @PathVariable Long shoppingCartId,
			@RequestParam Long companyId) throws InstanceNotFoundException, PermissionException {

		return toShoppingCartDto(shoppingService.findShoppingCartProducts(userId, shoppingCartId, companyId));

	}

}
