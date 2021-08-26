package es.udc.tfgproject.backend.model.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.User;
import es.udc.tfgproject.backend.model.entities.User.RoleType;
import es.udc.tfgproject.backend.model.entities.UserDao;
import es.udc.tfgproject.backend.model.entities.WeeklyBalance;
import es.udc.tfgproject.backend.model.entities.WeeklyBalanceDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

@Service
@Transactional
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private WeeklyBalanceDao weeklyBalanceDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PermissionChecker permissionChecker;

	@Override
	public void payWeeklyBalanceToCompanies() {
		List<User> companyUsers = userDao.findByRole(RoleType.BUSINESSMAN);
		for (User user : companyUsers) {
			Integer year = LocalDate.now().getYear();
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			Integer weekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
			weekNumber = weekNumber - 1;

			// Se paga la semana anterior, por lo tanto la primera semana de Enero, se paga
			// la última de Diciembre
			if (weekNumber.equals(0)) {
				year = year - 1;
				weekNumber = LocalDate.ofYearDay(year, 31).get(weekFields.weekOfWeekBasedYear());

			}

			Optional<WeeklyBalance> weeklyBalanceOptional = weeklyBalanceDao
					.findByWeekNumberAndYearAndUserId(weekNumber, year, user.getId());
			System.out.println("Llego aquí");
			if (weeklyBalanceOptional.isPresent()) {
				WeeklyBalance weeklyBalance = weeklyBalanceOptional.get();
				BigDecimal newGlobalBalance = user.getGlobalBalance().add(weeklyBalance.getBalance());
				user.setGlobalBalance(newGlobalBalance);
			}

		}

	}

	@Override
	public void payQuarterlyFee(Long userId) throws InstanceNotFoundException {
		User user = permissionChecker.checkUser(userId);

		if (RoleType.BUSINESSMAN.equals(user.getRole())) {
			BigDecimal globalBalance = user.getGlobalBalance().subtract(Constantes.RELIVRY_COMPANY_FEE);
			user.setGlobalBalance(globalBalance);
		}
	}

}
