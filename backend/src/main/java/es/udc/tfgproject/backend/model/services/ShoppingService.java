package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
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

	Order buy(Long userId, Long shoppingCartId, Long companyId, Boolean homeSale, String street, String cp, Long cityId,
			Boolean saveAsFavAddress) throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException;

	Order findOrder(Long userId, Long orderId) throws InstanceNotFoundException, PermissionException;

	Block<Order> findUserOrders(Long userId, int page, int size);

	Block<Order> findCompanyOrders(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

}
