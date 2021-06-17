package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;

import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketHasExpiredException;
import es.udc.tfgproject.backend.model.exceptions.DiscountTicketUsedException;
import es.udc.tfgproject.backend.model.exceptions.EmptyShoppingCartException;
import es.udc.tfgproject.backend.model.exceptions.IncorrectDiscountCodeException;
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

	ShoppingCart changeShoppingCartHomeSale(Long userId, Long shoppingCartId, Long companyId, Boolean homeSale)
			throws InstanceNotFoundException, PermissionException;

	Order buy(Long userId, Long shoppingCartId, Long companyId, Boolean homeSale, String street, String cp,
			String codeDiscount) throws InstanceNotFoundException, PermissionException, EmptyShoppingCartException,
			IncorrectDiscountCodeException, DiscountTicketHasExpiredException, DiscountTicketUsedException;

	Order findOrder(Long userId, Long orderId) throws InstanceNotFoundException, PermissionException;

	Block<Order> findUserOrders(Long userId, int page, int size);

	Block<Order> findCompanyOrders(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException;

	// Canjear cupon
	BigDecimal redeemDiscountTicket(Long userId, Long companyId, Long shoppingCartId, String code)
			throws InstanceNotFoundException, PermissionException, IncorrectDiscountCodeException,
			EmptyShoppingCartException, DiscountTicketHasExpiredException, DiscountTicketUsedException;

	// buscarCupones
	Block<DiscountTicket> findDiscountTickets(Long userId, Long companyId);

	// añadir goals
	Goal addGoal(Long companyId, BigDecimal discountCash, int discountPercentage, GoalType goalType, int goal);

}
