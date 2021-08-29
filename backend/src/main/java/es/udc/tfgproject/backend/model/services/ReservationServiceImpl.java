package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.EventEvaluation;
import es.udc.tfgproject.backend.model.entities.EventEvaluationDao;
import es.udc.tfgproject.backend.model.entities.Menu;
import es.udc.tfgproject.backend.model.entities.MenuItem;
import es.udc.tfgproject.backend.model.entities.MenuItemDao;
import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.Reserve;
import es.udc.tfgproject.backend.model.entities.Reserve.PeriodType;
import es.udc.tfgproject.backend.model.entities.ReserveDao;
import es.udc.tfgproject.backend.model.entities.ReserveItem;
import es.udc.tfgproject.backend.model.entities.ReserveItemDao;
import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.WeeklyBalance;
import es.udc.tfgproject.backend.model.entities.WeeklyBalanceDao;
import es.udc.tfgproject.backend.model.exceptions.CompanyDoesntAllowReservesException;
import es.udc.tfgproject.backend.model.exceptions.EmptyMenuException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.MaximumCapacityExceededException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.exceptions.ReservationDateIsBeforeNowException;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private MenuItemDao menuItemDao;

	@Autowired
	private ReserveItemDao reserveItemDao;

	@Autowired
	private ReserveDao reserveDao;

	@Autowired
	private EventEvaluationDao eventEvaluationDao;

	@Autowired
	private WeeklyBalanceDao weeklyBalanceDao;

	@Override
	public Menu addToMenu(Long userId, Long menuId, Long productId, Long companyId, int quantity)
			throws InstanceNotFoundException, PermissionException {

		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, userId);
		Optional<Product> product = productDao.findById(productId);

		if (!product.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		Optional<MenuItem> existingMenuItem = menu.getItem(productId);

		if (existingMenuItem.isPresent()) {
			existingMenuItem.get().incrementQuantity(quantity);
		} else {
			MenuItem newMenuItem = new MenuItem(product.get(), menu, quantity);
			menu.addItem(newMenuItem);
			menuItemDao.save(newMenuItem);
		}

		return filterMenu(menu, companyId);
	}

	@Override
	public Menu updateMenuItemQuantity(Long userId, Long menuId, Long productId, Long companyId, int quantity)
			throws InstanceNotFoundException, PermissionException {
		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, userId);

		Optional<MenuItem> existingMenuItem = menu.getItem(productId);

		if (!existingMenuItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		existingMenuItem.get().setQuantity(quantity);

		return filterMenu(menu, companyId);
	}

	@Override
	public Menu removeMenuItem(Long userId, Long menuId, Long productId, Long companyId)
			throws InstanceNotFoundException, PermissionException {
		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, userId);

		Optional<MenuItem> existingMenuItem = menu.getItem(productId);

		if (!existingMenuItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}
		menu.removeItem(existingMenuItem.get());
		menuItemDao.delete(existingMenuItem.get());

		return filterMenu(menu, companyId);
	}

	@Override
	@Transactional(readOnly = true)
	public Menu findMenuProducts(Long userId, Long menuId, Long companyId)
			throws InstanceNotFoundException, PermissionException {

		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, userId);
		return filterMenu(menu, companyId);
	}

	@Override
	public Reserve reservation(Long userId, Long menuId, Long companyId, LocalDate reservationDate, Integer diners,
			PeriodType periodType) throws InstanceNotFoundException, PermissionException, EmptyMenuException,
			MaximumCapacityExceededException, ReservationDateIsBeforeNowException, CompanyDoesntAllowReservesException {
		User user = permissionChecker.checkUser(userId);
		Company company = permissionChecker.checkCompany(companyId);

		if (!company.getReserve()) {
			throw new CompanyDoesntAllowReservesException();
		}

		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, user.getId());

		if (menu.isEmpty()) {
			throw new EmptyMenuException();
		}

		menu = filterMenu(menu, company.getId());

		checkCapacity(company.getId(), reservationDate, periodType, diners);

		BigDecimal deposit = calculateDepositFromPercentage(companyId, menu.getTotalPrice());

		Reserve reserve = new Reserve(menu.getUser(), company, reservationDate, diners, periodType,
				menu.getTotalPrice(), deposit);

		reserveDao.save(reserve);

		setWeeklyBalance(company.getUser(), reserve.getDeposit());

		for (MenuItem menuItem : menu.getItems()) {

			ReserveItem reserveItem = new ReserveItem(menuItem.getProduct(), menuItem.getProduct().getPrice(),
					menuItem.getQuantity());

			reserve.addItem(reserveItem);
			reserveItemDao.save(reserveItem);
			menuItemDao.delete(menuItem);

		}

		menu.removeAll();

		EventEvaluation eventEvaluation = new EventEvaluation(reserve, reservationDate.plusDays(Constantes.NUMERO_UNO));

		eventEvaluationDao.save(eventEvaluation);

		return reserve;
	}

	private void setWeeklyBalance(User user, BigDecimal deposit) {
		Integer year = LocalDate.now().getYear();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		Integer weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

		BigDecimal balanceToCompany = deposit.multiply(Constantes.RELIVRY_PERCENTAGE);

		Optional<WeeklyBalance> weeklyBalanceOptional = weeklyBalanceDao.findByWeekNumberAndYearAndUserId(weekNumber,
				year, user.getId());

		if (!weeklyBalanceOptional.isPresent()) {
			WeeklyBalance balance = new WeeklyBalance(balanceToCompany, weekNumber, year, user);
			weeklyBalanceDao.save(balance);
		} else {
			WeeklyBalance weeklyBalance = weeklyBalanceOptional.get();
			BigDecimal newBalance = balanceToCompany.add(weeklyBalance.getBalance());
			weeklyBalance.setBalance(newBalance);
		}

	}

	@Override
	public BigDecimal calculateDepositFromPercentage(Long companyId, BigDecimal totalPrice)
			throws InstanceNotFoundException {
		Company company = permissionChecker.checkCompany(companyId);

		double percentage = (double) company.getReservePercentage() / 100;
		BigDecimal valorToDiscount = totalPrice.multiply(new BigDecimal(percentage));

		return valorToDiscount.setScale(Constantes.SCALE, RoundingMode.FLOOR);
	}

	@Override
	@Transactional(readOnly = true)
	public Reserve findReserve(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException {
		return permissionChecker.checkReserveExistsAndBelongsToUserOrCompany(reserveId, userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Reserve> findUserReserves(Long userId, int page, int size) {
		Slice<Reserve> slice = reserveDao.findByUserIdOrderByDateAsc(userId, PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Reserve> findCompanyReserves(Long userId, Long companyId, LocalDate date, PeriodType periodType,
			int page, int size) throws InstanceNotFoundException, PermissionException {
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<Reserve> slice = reserveDao.findByCompanyIdAndDateAndPeriodTypeOrderByDateDesc(company.getId(), date,
				periodType, PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

	// Devuelve el menú con los productos que pertenezcan a una compañía
	private Menu filterMenu(Menu menu, Long companyId) {
		Set<MenuItem> items = new HashSet<>();
		items.addAll(menuItemDao.findByProductCompanyId(companyId));
		menu.setItems(items);

		return menu;
	}

	@Override
	public Integer obtainMaxDinersAllowed(Long companyId, LocalDate reservationDate, PeriodType periodType)
			throws InstanceNotFoundException {
		Company company = permissionChecker.checkCompany(companyId);

		Integer dinersNow = reserveDao.getSumDinersByReservationDateAndPeriodType(reservationDate, periodType);

		Integer dinersAllowed = company.getCapacity() - dinersNow;

		return dinersAllowed;
	}

	@Override
	public Boolean checkCapacity(Long companyId, LocalDate reservationDate, PeriodType periodType, Integer diners)
			throws MaximumCapacityExceededException, InstanceNotFoundException, ReservationDateIsBeforeNowException {

		if (reservationDate.isBefore(LocalDate.now())) {
			throw new ReservationDateIsBeforeNowException();
		}

		Integer dinersAllowed = obtainMaxDinersAllowed(companyId, reservationDate, periodType);

		if ((dinersAllowed - diners) < Constantes.NUMERO_CERO) {
			throw new MaximumCapacityExceededException();
		}

		return true;
	}

	@Override
	public void cancelReservation(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException {
		User user = permissionChecker.checkUser(userId);
		Reserve reserve = permissionChecker.checkReserveExistsAndBelongsToUser(reserveId, user.getId());
		EventEvaluation eventEvaluation = permissionChecker.checkEventEvaluationBelongsToReserve(reserve.getId());

		// Si la cancelación se realiza el mismo día, se elimina la reserva y no se
		// reembolsa la señal
		// Sino, se eliminia y reembolsa la señal
		if (!reserve.getDate().equals(LocalDate.now())) {
			user.setGlobalBalance(user.getGlobalBalance().add(reserve.getDeposit()));

			Integer year = LocalDate.now().getYear();
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			Integer weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
			BigDecimal balanceToCompany = reserve.getDeposit().multiply(Constantes.RELIVRY_PERCENTAGE);
			Optional<WeeklyBalance> weeklyBalanceOptional = weeklyBalanceDao
					.findByWeekNumberAndYearAndUserId(weekNumber, year, reserve.getCompany().getUser().getId());

			if (!weeklyBalanceOptional.isPresent()) {
				balanceToCompany = new BigDecimal(0).subtract(balanceToCompany);
				WeeklyBalance weeklyBalance = new WeeklyBalance(balanceToCompany, weekNumber, year,
						reserve.getCompany().getUser());
				weeklyBalanceDao.save(weeklyBalance);
			} else {
				WeeklyBalance weeklyBalance = weeklyBalanceOptional.get();
				weeklyBalance.setBalance(weeklyBalance.getBalance().subtract(balanceToCompany));
			}
		}
		for (ReserveItem reserveItem : reserve.getItems()) {
			reserveItemDao.delete(reserveItem);
		}
		eventEvaluationDao.delete(eventEvaluation);
		reserveDao.delete(reserve);

	}

	@Override
	public void addEventEvaluation(Long userId, Long eventEvaluationId, Integer points, String opinion)
			throws PermissionException, InstanceNotFoundException {

		EventEvaluation eventEvaluation = permissionChecker
				.checkEventEvaluationExistsAndBelongsToUser(eventEvaluationId, userId);

		eventEvaluation.setPoints(points);
		eventEvaluation.setOpinion(opinion);
		eventEvaluation.setDone(true);

	}

	@Override
	@Transactional(readOnly = true)
	public Block<EventEvaluation> findCompanyEventEvaluations(Long userId, Long companyId, int page, int size)
			throws PermissionException, InstanceNotFoundException {
		Company company = permissionChecker.checkCompanyExistsAndBelongsToUser(companyId, userId);

		Slice<EventEvaluation> slice = eventEvaluationDao
				.findByReserveCompanyIdAndDoneOrderByDateEvaluation(company.getId(), true, PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());

	}

	@Override
	public Block<EventEvaluation> findUserEventEvaluations(Long userId, int page, int size)
			throws PermissionException, InstanceNotFoundException {
		Slice<EventEvaluation> slice = eventEvaluationDao.findByReserveUserIdAndDoneOrderByDateEvaluation(userId,
				LocalDate.now(), PageRequest.of(page, size));

		return new Block<>(slice.getContent(), slice.hasNext());
	}

}
