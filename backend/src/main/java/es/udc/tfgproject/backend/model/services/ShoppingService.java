package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface ShoppingService {

	ShoppingCart addToShoppingCart(Long userId, Long shoppingCartId, Long productId, Long companyId, int quantity)
			throws InstanceNotFoundException, PermissionException;

	ShoppingCart updateShoppingCartItemQuantity(Long userId, Long shoppingCartId, Long productId, Long companyId,
			int quantity) throws InstanceNotFoundException, PermissionException;

	ShoppingCart removeShoppingCartItem(Long userId, Long shoppingCartId, Long productId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

	ShoppingCart findShoppingCartProducts(Long userId, Long shoppingCartId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

}
