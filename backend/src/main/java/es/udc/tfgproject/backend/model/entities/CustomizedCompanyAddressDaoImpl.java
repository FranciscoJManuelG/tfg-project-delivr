package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class CustomizedCompanyAddressDaoImpl implements CustomizedCompanyAddressDao {

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
	public Slice<CompanyAddress> find(Long companyCategoryId, Long cityId, String street, String keywords, int page,
			int size) {
		String[] tokens = getTokens(keywords);
		String[] streetTokens = getTokens(street);
		String queryString = "SELECT ca FROM CompanyAddress ca";

		if (companyCategoryId != null || cityId != null || streetTokens.length > 0 || tokens.length > 0) {
			queryString += " WHERE ";
		}

		if (companyCategoryId != null) {
			queryString += "ca.company.companyCategory.id = :companyCategoryId";
		}

		if (cityId != null) {

			if (companyCategoryId != null) {
				queryString += " AND ";
			}

			queryString += "ca.city.id = :cityId";
		}

		if (streetTokens.length != 0) {

			if (companyCategoryId != null || cityId != null) {
				queryString += " AND ";
			}

			for (int i = 0; i < streetTokens.length - 1; i++) {
				queryString += "LOWER(ca.street) LIKE LOWER(:streetToken" + i + ") AND ";
			}

			queryString += "LOWER(ca.street) LIKE LOWER(:streetToken" + (streetTokens.length - 1) + ")";

		}

		if (tokens.length != 0) {

			if (companyCategoryId != null || cityId != null || streetTokens.length != 0) {
				queryString += " AND ";
			}

			for (int i = 0; i < tokens.length - 1; i++) {
				queryString += "LOWER(ca.company.name) LIKE LOWER(:token" + i + ") AND ";
			}

			queryString += "LOWER(ca.company.name) LIKE LOWER(:token" + (tokens.length - 1) + ")";

		}

		queryString += " ORDER BY ca.company.name";

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

		List<CompanyAddress> companiesAddresses = query.getResultList();
		boolean hasNext = companiesAddresses.size() == (size + 1);

		if (hasNext) {
			companiesAddresses.remove(companiesAddresses.size() - 1);
		}

		return new SliceImpl<>(companiesAddresses, PageRequest.of(page, size), hasNext);

	}

}
