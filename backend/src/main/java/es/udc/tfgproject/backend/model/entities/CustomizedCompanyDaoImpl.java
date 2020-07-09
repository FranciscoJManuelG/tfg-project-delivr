package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class CustomizedCompanyDaoImpl implements CustomizedCompanyDao {

	@PersistenceContext
	private EntityManager entityManager;

	private String[] getTokens(String keywords) {

		if (keywords == null || keywords.length() == 0) {
			return new String[0];
		} else {
			return keywords.split("\\s");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Slice<Company> find(Long companyCategoryId, Long cityId, String street, String keywords, int page,
			int size) {
		String[] tokens = getTokens(keywords);
		String[] streetTokens = getTokens(street);
		String queryString = "SELECT c FROM Company c";

		if (companyCategoryId != null || cityId != null || street.length() > 0 || tokens.length > 0) {
			queryString += " WHERE ";
		}

		if (companyCategoryId != null) {
			queryString += "c.companyCategory.id = :companyCategoryId";
		}

		if (cityId != null) {

			if (companyCategoryId != null) {
				queryString += " AND ";
			}

			queryString += "c.addresses.city.id = :cityId";
		}

		if (streetTokens.length != 0) {

			if (companyCategoryId != null || cityId != null) {
				queryString += " AND ";
			}

			for (int i = 0; i < streetTokens.length - 1; i++) {
				queryString += "LOWER(c.street) LIKE LOWER(:streetToken" + i + ") AND ";
			}

			queryString += "LOWER(c.street) LIKE LOWER(:streetToken" + (streetTokens.length - 1) + ")";

		}

		if (tokens.length != 0) {

			if (companyCategoryId != null || cityId != null || streetTokens.length != 0) {
				queryString += " AND ";
			}

			for (int i = 0; i < tokens.length - 1; i++) {
				queryString += "LOWER(c.name) LIKE LOWER(:token" + i + ") AND ";
			}

			queryString += "LOWER(c.name) LIKE LOWER(:token" + (tokens.length - 1) + ")";

		}

		queryString += " ORDER BY c.name";

		Query query = entityManager.createQuery(queryString).setFirstResult(page * size).setMaxResults(size + 1);

		if (companyCategoryId != null) {
			query.setParameter("companyCategoryId", companyCategoryId);
		}

		if (cityId != null) {
			query.setParameter("cityId", cityId);
		}

		if (streetTokens.length != 0) {
			for (int i = 0; i < streetTokens.length; i++) {
				query.setParameter("streetToken" + i, '%' + streetTokens[i] + '%');
			}
		}

		if (tokens.length != 0) {
			for (int i = 0; i < tokens.length; i++) {
				query.setParameter("token" + i, '%' + tokens[i] + '%');
			}

		}

		List<Company> companies = query.getResultList();
		boolean hasNext = companies.size() == (size + 1);

		if (hasNext) {
			companies.remove(companies.size() - 1);
		}

		return new SliceImpl<>(companies, PageRequest.of(page, size), hasNext);

	}

}
