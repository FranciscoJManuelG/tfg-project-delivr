package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

		if(!company.getReserve()){
			throw new CompanyDoesntAllowReservesException();
		}

		Menu menu = permissionChecker.checkMenuExistsAndBelongsToUser(menuId, user.getId());

		if (menu.isEmpty()) {
			throw new EmptyMenuException();
		}

		checkCapacity(company.getId(), reservationDate, periodType, diners);

		BigDecimal deposit = calculateDepositFromPercentage(company.getReservePercentage(), menu.getTotalPrice());

		Reserve reserve = new Reserve(menu.getUser(), company, reservationDate, diners, periodType,
				menu.getTotalPrice(), deposit);

		reserveDao.save(reserve);

		menu = filterMenu(menu, company.getId());

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

	private BigDecimal calculateDepositFromPercentage(int reservePercentage, BigDecimal totalPrice) {
		double percentage = (double) reservePercentage / 100;
		BigDecimal valorToDiscount = totalPrice.multiply(new BigDecimal(percentage));

		return valorToDiscount;
	}

	@Override
	@Transactional(readOnly = true)
	public Reserve findReserve(Long userId, Long reserveId) throws InstanceNotFoundException, PermissionException {
		return permissionChecker.checkReserveExistsAndBelongsToUserOrCompany(reserveId, userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Block<Reserve> findUserReserves(Long userId, int page, int size) {
		Slice<Reserve> slice = reserveDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));

		List<Reserve> reserves = slice.getContent().stream()
				.filter(r -> !r.getDate().isBefore(LocalDate.now())).collect(Collectors.toList());

		return new Block<>(reserves, slice.hasNext());
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

		Integer dinersAllowed = company.getReserveCapacity() - dinersNow;

		return dinersAllowed;
	}

	@Override
	public Boolean checkCapacity(Long companyId, LocalDate reservationDate, PeriodType periodType, Integer diners)
			throws MaximumCapacityExceededException, InstanceNotFoundException, ReservationDateIsBeforeNowException {

		if(reservationDate.isBefore(LocalDate.now())){
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
		Reserve reserve = permissionChecker.checkReserveExistsAndBelongsToUser(reserveId, userId);
		EventEvaluation eventEvaluation = permissionChecker.checkEventEvaluationBelongsToReserve(reserveId);
		// Cuando implemente lo de PayPal, hacer comprobación de que cuando cancela es
		// antes que el día anterior del evento.
		// Si se cancela el mismo día, no se realiza el reembolso del depoito por
		// adelantado.

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
		Slice<EventEvaluation> slice = eventEvaluationDao.findByReserveUserIdAndDoneOrderByDateEvaluation(userId, false,
				PageRequest.of(page, size));

		List<EventEvaluation> evaluations = slice.getContent().stream()
				.filter(e -> e.getDateEvaluation().isBefore(LocalDate.now())).collect(Collectors.toList());

		return new Block<>(evaluations, slice.hasNext());
	}

}
