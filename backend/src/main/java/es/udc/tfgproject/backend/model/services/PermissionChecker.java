package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.FavouriteAddress;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

public interface PermissionChecker {

	public void checkUserExists(Long userId) throws InstanceNotFoundException;

	public User checkUser(Long userId) throws InstanceNotFoundException;

	public Company checkCompanyExistsAndBelongsToUser(Long companyId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Company checkCompanyExistsAndUserOrAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public Company checkCompanyExistsAndOnlyAdminCanModify(Long userId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public CompanyAddress checkCompanyAddressExistsAndBelongsToUser(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public FavouriteAddress checkFavouriteAddressExistsAndBelongsToUser(Long addressId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Product checkProductExistsAndBelongsToCompany(Long productId, Long companyId)
			throws PermissionException, InstanceNotFoundException;

	public Product checkProductExistsAndBelongsToUser(Long productId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public ShoppingCart checkShoppingCartExistsAndBelongsToUser(Long shoppingCartId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Order checkOrderExistsAndBelongsToUser(Long orderId, Long userId)
			throws InstanceNotFoundException, PermissionException;

	public DiscountTicket checkDiscountTicketExistsAndBelongsToUser(String code, Long userId)
			throws InstanceNotFoundException, PermissionException;

	public Goal checkGoalAndBelongsToCompany(Long goalId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

	public Goal checkGoalExistsAndBelongsToCompany(Long goalId, Long companyId)
			throws InstanceNotFoundException, PermissionException;

	public GoalType checkGoalType(Long goalTypeId) throws InstanceNotFoundException, PermissionException;

	public Company checkCompany(Long companyId) throws InstanceNotFoundException;

	public Menu checkMenuExistsAndBelongsToUser(Long menuId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public Reserve checkReserveExistsAndBelongsToUser(Long reserveId, Long userId)
			throws PermissionException, InstanceNotFoundException;

	public EventEvaluation checkEventEvaluationBelongsToReserve(Long reserveId) throws InstanceNotFoundException;

	public EventEvaluation checkEventEvaluationExistsAndBelongsToUser(Long eventEvaluationId, Long userId)
			throws InstanceNotFoundException, PermissionException;

}
