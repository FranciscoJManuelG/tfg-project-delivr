package es.udc.tfgproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.UserDao;
import es.udc.tfgproject.backend.model.entities.WeeklyBalance;
import es.udc.tfgproject.backend.model.entities.WeeklyBalanceDao;
import es.udc.tfgproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.BalanceService;
import es.udc.tfgproject.backend.model.services.Constantes;
import es.udc.tfgproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BalanceServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private BalanceService balanceService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private WeeklyBalanceDao weeklyBalanceDao;

	private User signUpUserBusiness(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "email@gmail.com", "123456789");

		try {
			userService.signUpBusinessman(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	private User signUpUser(String userName) {

		User user = new User(userName, "passwd", "firstName", "lastName", "email@gmail.com", "123456789");

		try {
			userService.signUp(user);
		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

		return user;

	}

	@Test
	public void testPayWeeklyBalanceToCompanies() throws InstanceNotFoundException {

		User user1 = signUpUserBusiness("user");
		User user2 = signUpUserBusiness("user2");
		User user3 = signUpUserBusiness("user3");
		User user4 = signUpUser("user4");

		BigDecimal balance1 = new BigDecimal(200);
		BigDecimal balance2 = new BigDecimal(150);

		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		Integer weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

		WeeklyBalance weeklyBalanceUser1 = new WeeklyBalance(balance1, weekNumber - 1, 2021, user1);
		WeeklyBalance weeklyBalanceUser2 = new WeeklyBalance(balance2, weekNumber - 1, 2021, user2);
		WeeklyBalance weeklyBalanceUser3 = new WeeklyBalance(balance1, weekNumber, 2021, user3);

		weeklyBalanceDao.save(weeklyBalanceUser1);
		weeklyBalanceDao.save(weeklyBalanceUser2);
		weeklyBalanceDao.save(weeklyBalanceUser3);

		balanceService.payWeeklyBalanceToCompanies();

		assertEquals(user1.getGlobalBalance(), balance1);
		assertEquals(user2.getGlobalBalance(), balance2);

		BigDecimal zero = new BigDecimal(0);

		assertEquals(user3.getGlobalBalance(), zero);
		assertEquals(user4.getGlobalBalance(), zero);

	}

	@Test
	public void testChargeQuarterlyFee() throws InstanceNotFoundException {

		User user = signUpUserBusiness("user");
		balanceService.payQuarterlyFee(user.getId());

		BigDecimal expected = new BigDecimal(0).subtract(Constantes.RELIVRY_COMPANY_FEE);
		assertEquals(expected, userDao.findById(user.getId()).get().getGlobalBalance());

	}

}
