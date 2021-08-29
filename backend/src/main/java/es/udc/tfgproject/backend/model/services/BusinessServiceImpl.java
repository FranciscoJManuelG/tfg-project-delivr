package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyCategory;
import es.udc.tfgproject.backend.model.entities.CompanyCategoryDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.entities.DiscountTicket;
import es.udc.tfgproject.backend.model.entities.DiscountTicket.DiscountType;
import es.udc.tfgproject.backend.model.entities.DiscountTicketDao;
import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.EventEvaluationDao;
import es.udc.tfgproject.backend.model.entities.Goal;
import es.udc.tfgproject.backend.model.entities.GoalDao;
import es.udc.tfgproject.backend.model.entities.GoalType;
import es.udc.tfgproject.backend.model.entities.GoalTypeDao;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuDao;
import es.udc.tfgproject.backend.model.entities.MenuItem;
import es.udc.tfgproject.backend.model.entities.MenuItemDao;
import es.udc.tfgproject.backend.model.entities.Order;
import es.udc.tfgproject.backend.model.entities.OrderDao;
import es.udc.tfgproject.backend.model.entities.OrderItem;
import es.udc.tfgproject.backend.model.entities.OrderItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.Province;
import es.udc.tfgproject.backend.model.entities.ProvinceDao;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.ReserveDao;
import es.udc.tfgproject.backend.model.entities.ReserveItem;
import es.udc.tfgproject.backend.model.entities.ReserveItemDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.UserDao;
import es.udc.tfgproject.backend.model.entities.WeeklyBalance;
import es.udc.tfgproject.backend.model.entities.WeeklyBalanceDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAddressDao companyAddressDao;

	@Autowired
	private CompanyCategoryDao companyCategoryDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private GoalDao goalDao;

	@Autowired
	private GoalTypeDao goalTypeDao;

	@Autowired
	private DiscountTicketDao discountTicketDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private ReserveDao reserveDao;

	@Autowired
	private ReserveItemDao reserveItemDao;

	@Autowired
	private EventEvaluationDao eventEvaluationDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private ShoppingCartDao shoppingCartDao;

	@Autowired
	private MenuItemDao menuItemDao;

	@Autowired
	private ShoppingCartItemDao shoppingCartItemDao;

	@Autowired
	private WeeklyBalanceDao weeklyBalanceDao;

	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public Company addCompany(Long userId, String name, Integer capacity, Boolean reserve, Boolean homeSale,
			Integer reservePercentage, Long companyCategoryId, LocalTime openingTime, LocalTime closingTime,
			LocalTime lunchTime, LocalTime dinerTime) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		Company company = new Company(user, name, capacity, reserve, homeSale, reservePercentage, false,
				companyCategory, openingTime, closingTime, lunchTime, dinerTime);
		companyDao.save(company);

		return company;
	}

	@Override
	public Company modifyCompany(Long userId, Long companyId, String name, Integer capacity, Boolean reserve,
			Boolean homeSale, Integer reservePercentage, Long companyCategoryId, LocalTime openingTime,
			LocalTime closingTime, LocalTime lunchTime, LocalTime dinerTime)
			throws InstanceNotFoundException, PermissionException {

		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		CompanyCategory companyCategory = checkCompanyCategory(companyCategoryId);

		company.setName(name);
		company.setReserve(reserve);
		company.setHomeSale(homeSale);
		company.setCompanyCategory(companyCategory);
		company.setOpeningTime(openingTime);
		company.setClosingTime(closingTime);
		if (capacity != null) {
			company.setCapacity(capacity);
		}
		if (reservePercentage != null) {
			company.setReservePercentage(reservePercentage);
		}
		if (lunchTime != null) {
			company.setLunchTime(lunchTime);
		}
		if (dinerTime != null) {
			company.setDinerTime(dinerTime);
		}

		return company;

	}

	/*
	 * TODO: Revisar si otro usuario, como el admin, puede bloquear o desbloquear
	 * una compañía
	 */

	@Override
	public Company blockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUser(userId);
		Company company = permissionChecker.checkCompanyExistsAndUserOrAdminCanModify(userId, companyId);

		company.setBlock(true);

		return company;

	}

	@Override
	public Company unlockCompany(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUser(userId);
		Company company = permissionChecker.checkCompanyExistsAndUserOrAdminCanModify(userId, companyId);

		company.setBlock(false);

		return company;

	}

	@Override
	public void deregister(Long userId, Long companyId) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndOnlyAdminCanModify(userId, companyId);
		Company company = permissionChecker.checkCompany(companyId);
		List<CompanyAddress> companyAddresses = companyAddressDao.findByCompanyId(companyId);
		List<Goal> goals = goalDao.findByCompanyId(companyId);
		List<Order> orders = orderDao.findByCompanyId(company.getId());
		List<Reserve> reserves = reserveDao.findByCompanyId(company.getId());
		List<Product> products = productDao.findByCompanyId(companyId);

		deleteCompanyAdresses(companyAddresses);
		deleteOrdersAndOrderItems(orders);
		deleteReservesAndReserveItemsAndEvaluations(reserves);
		deleteGoalsAndDiscountTicket(goals);
		deleteMenuAndMenuItems(company);
		deleteCartAndCartItems(company);
		deleteProducts(products);
		companyDao.delete(company);
		deleteWeeklyBalances(company);
		userDao.delete(company.getUser());

	}

	private void deleteWeeklyBalances(Company company) {
		List<WeeklyBalance> balances = weeklyBalanceDao.findByUserId(company.getUser().getId());
		for (WeeklyBalance weeklyBalance : balances) {
			weeklyBalanceDao.delete(weeklyBalance);
		}

	}

	private void deleteProducts(List<Product> products) {
		for (Product product : products) {
			productDao.delete(product);
		}

	}

	private void deleteCartAndCartItems(Company company) {
		ShoppingCart cart = shoppingCartDao.findByUserId(company.getUser().getId());
		List<ShoppingCartItem> itemsCart = shoppingCartItemDao.findByShoppingCartId(cart.getId());
		for (ShoppingCartItem shoppingCartItem : itemsCart) {
			shoppingCartItemDao.delete(shoppingCartItem);
		}
		shoppingCartDao.delete(cart);
	}

	private void deleteMenuAndMenuItems(Company company) {
		Menu menu = menuDao.findByUserId(company.getUser().getId());
		List<MenuItem> itemsMenu = menuItemDao.findByMenuId(menu.getId());
		for (MenuItem menuItem : itemsMenu) {
			System.out.println("LLego aqui");
			menuItemDao.delete(menuItem);
			System.out.println("LLego aqui");

		}
		menuDao.delete(menu);
	}

	private void deleteGoalsAndDiscountTicket(List<Goal> goals) {
		for (Goal goal : goals) {
			List<DiscountTicket> tickets = discountTicketDao.findByGoalId(goal.getId());
			for (DiscountTicket ticket : tickets) {
				discountTicketDao.delete(ticket);
			}
			goalDao.delete(goal);
		}

	}

	private void deleteReservesAndReserveItemsAndEvaluations(List<Reserve> reserves) {
		for (Reserve reserve : reserves) {
			List<ReserveItem> items = reserveItemDao.findByReserveId(reserve.getId());
			for (ReserveItem item : items) {
				reserveItemDao.delete(item);
			}
			EventEvaluation evaluation = eventEvaluationDao.findByReserveId(reserve.getId()).get();
			eventEvaluationDao.delete(evaluation);
			reserveDao.delete(reserve);
		}
	}

	private void deleteOrdersAndOrderItems(List<Order> orders) {
		for (Order order : orders) {
			List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
			for (OrderItem item : items) {
				orderItemDao.delete(item);
			}
			orderDao.delete(order);
		}

	}

	private void deleteCompanyAdresses(List<CompanyAddress> companyAddresses) {
		for (CompanyAddress companyAddress : companyAddresses) {
			companyAddressDao.delete(companyAddress);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Company findCompany(Long userId) throws InstanceNotFoundException {
		User user = permissionChecker.checkUser(userId);

		Company company = companyDao.findByUserId(user.getId());

		return company;
	}

	@Override
	@Transactional(readOnly = true)
	public Company findCompanyById(Long userId, Long companyId) throws InstanceNotFoundException {
		permissionChecker.checkUser(userId);

		Company company = companyDao.findById(companyId).get();

		return company;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompanyCategory> findAllCompanyCategories() {

		Iterable<CompanyCategory> categories = companyCategoryDao.findAll();
		List<CompanyCategory> categoriesList = new ArrayList<>();

		categories.forEach(category -> categoriesList.add(category));

		return categoriesList;

	}

	@Override
	public CompanyCategory addCompanyCategory(Long userId, String name)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);

		CompanyCategory category = new CompanyCategory(name);
		companyCategoryDao.save(category);

		return category;

	}

	@Override
	public CompanyCategory modifyCompanyCategory(Long userId, Long companyCategoryId, String name)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Optional<CompanyCategory> category = companyCategoryDao.findById(companyCategoryId);

		if (!category.isPresent()) {
			throw new InstanceNotFoundException("project.entities.companyCategory", companyCategoryId);

		}

		CompanyCategory companyCategory = category.get();
		companyCategory.setName(name);

		return companyCategory;
	}

	@Override
	public CompanyAddress addCompanyAddress(String street, String cp, Long cityId, Long companyId)
			throws InstanceNotFoundException {

		City city = checkCity(cityId);
		Company company = checkCompany(companyId);

		CompanyAddress companyAddress = new CompanyAddress();

		companyAddress.setCompany(company);
		companyAddress.setCity(city);
		companyAddress.setCp(cp);
		companyAddress.setStreet(street);

		companyAddressDao.save(companyAddress);

		return companyAddress;
	}

	@Override
	public void deleteCompanyAddress(Long userId, Long addressId)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyAddressExistsAndBelongsToUser(addressId, userId);

		companyAddressDao.delete(companyAddressDao.findByAddressId(addressId).get());

	}

	@Override
	@Transactional(readOnly = true)
	public Block<CompanyAddress> findCompanyAddresses(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException {

		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<CompanyAddress> slice = companyAddressDao.findByCompanyId(company.getId(), PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());

	}

	@Override
	@Transactional(readOnly = true)
	public List<City> findAllCities() {

		Iterable<City> cities = cityDao.findAll();
		List<City> citiesList = new ArrayList<>();

		cities.forEach(city -> citiesList.add(city));

		return citiesList;

	}

	@Override
	public City addCity(Long userId, Long provinceId, String name)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Province province = permissionChecker.checkProvinceExists(provinceId);

		City city = new City(name, province);
		cityDao.save(city);

		return city;

	}

	@Override
	public City modifyCity(Long userId, Long cityId, Long provinceId, String name)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Province province = permissionChecker.checkProvinceExists(provinceId);
		Optional<City> cityOptional = cityDao.findById(cityId);

		if (!cityOptional.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);

		}

		City city = cityOptional.get();
		city.setName(name);
		city.setProvince(province);

		return city;
	}

	@Override
	public void removeCity(Long userId, Long cityId) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Optional<City> cityOptional = cityDao.findById(cityId);

		if (!cityOptional.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);

		}

		City city = cityOptional.get();
		cityDao.delete(city);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Province> findAllProvinces() {

		Iterable<Province> cities = provinceDao.findAll();
		List<Province> provincesList = new ArrayList<>();

		cities.forEach(province -> provincesList.add(province));

		return provincesList;

	}

	@Override
	public Province addProvince(Long userId, String name) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);

		Province province = new Province(name);
		provinceDao.save(province);

		return province;

	}

	@Override
	public Province modifyProvince(Long userId, Long provinceId, String name)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Province province = permissionChecker.checkProvinceExists(provinceId);

		province.setName(name);

		return province;
	}

	@Override
	public void removeProvince(Long userId, Long provinceId) throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkUserExistsAndIsAdmin(userId);
		Province province = permissionChecker.checkProvinceExists(provinceId);

		List<City> cities = cityDao.findByProvinceId(provinceId);

		for (City city : cities) {
			cityDao.delete(city);
		}
		provinceDao.delete(province);
	}

	/*
	 * Método privado que comprueba que la ciudad está registrada en el sistema
	 */
	private City checkCity(Long cityId) throws InstanceNotFoundException {
		Optional<City> cityOpt = cityDao.findById(cityId);
		City city = null;

		if (!cityOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);
		} else
			city = cityOpt.get();

		return city;
	}

	/*
	 * Método privado que comprueba que la categoría está registrada en el sistema
	 */
	private CompanyCategory checkCompanyCategory(Long companyCategoryId) throws InstanceNotFoundException {
		Optional<CompanyCategory> companyCategoryOpt = companyCategoryDao.findById(companyCategoryId);
		CompanyCategory companyCategory = null;

		if (!companyCategoryOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.companyCategory", companyCategoryId);
		} else
			companyCategory = companyCategoryOpt.get();

		return companyCategory;

	}

	/*
	 * Método privado que comprueba que la empresa está registrada en el sistema
	 */
	private Company checkCompany(Long companyId) throws InstanceNotFoundException {
		Optional<Company> companyOpt = companyDao.findById(companyId);
		Company company = null;

		if (!companyOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		} else
			company = companyOpt.get();

		return company;
	}

	@Override
	public Goal addGoal(Long userId, Long companyId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException {
		Goal goal = new Goal();
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		GoalType goalType = permissionChecker.checkGoalType(goalTypeId);

		switch (discountType) {
		case CASH:
			goal = new Goal(discountCash, 0, company, goalType, goalQuantity);
			break;
		case PERCENTAGE:
			goal = new Goal(new BigDecimal(0), discountPercentage, company, goalType, goalQuantity);
			break;

		default:
		}

		goalDao.save(goal);
		return goal;
	}

	@Override
	public Goal modifyGoal(Long userId, Long companyId, Long goalId, DiscountType discountType, BigDecimal discountCash,
			Integer discountPercentage, Long goalTypeId, int goalQuantity)
			throws InstanceNotFoundException, PermissionException {

		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Goal goal = permissionChecker.checkGoalAndBelongsToCompany(goalId, companyId);
		GoalType goalType = permissionChecker.checkGoalType(goalTypeId);

		switch (discountType) {
		case CASH:
			goal.setDiscountCash(discountCash);
			goal.setDiscountPercentage(0);
			goal.setGoalQuantity(goalQuantity);
			goal.setGoalType(goalType);
			break;

		case PERCENTAGE:
			goal.setDiscountPercentage(discountPercentage);
			goal.setDiscountCash(new BigDecimal(0));
			goal.setGoalQuantity(goalQuantity);
			goal.setGoalType(goalType);
			break;

		default:
			break;
		}

		return goal;
	}

	@Override
	@Transactional(readOnly = true)
	public Goal findGoal(Long userId, Long companyId, Long goalId)
			throws InstanceNotFoundException, PermissionException {

		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		return permissionChecker.checkGoalExistsAndBelongsToCompany(goalId, company.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Goal> findCompanyGoals(Long userId, Long companyId, int page, int size)
			throws InstanceNotFoundException, PermissionException {
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<Goal> slice = goalDao.findByCompanyIdOrderByIdDesc(company.getId(), PageRequest.of(page, size));
		return new Block<>(slice.getContent(), slice.hasNext());
	}

	@Override
	public Goal modifyStateGoal(Long userId, Long companyId, Long goalId, String option)
			throws InstanceNotFoundException, PermissionException {
		permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);
		Goal goal = permissionChecker.checkGoalExistsAndBelongsToCompany(goalId, companyId);

		switch (option) {
		case Constantes.ACTIVAR:
			goal.setActive(true);
			break;
		case Constantes.DESACTIVAR:
			goal.setActive(false);
			break;
		default:
			break;
		}

		return goal;

	}

	@Override
	@Transactional(readOnly = true)
	public List<GoalType> findAllGoalTypes() {

		Iterable<GoalType> goalTypes = goalTypeDao.findAll();
		List<GoalType> goalTypesList = new ArrayList<>();

		goalTypes.forEach(goalType -> goalTypesList.add(goalType));

		return goalTypesList;

	}

}
