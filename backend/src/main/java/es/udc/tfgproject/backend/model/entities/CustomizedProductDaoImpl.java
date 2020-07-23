package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class CustomizedProductDaoImpl implements CustomizedProductDao {

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
	public List<Product> find(Long companyId, Long productCategoryId, String keywords) {
		String[] tokens = getTokens(keywords);
		String queryString = "SELECT p FROM Product p WHERE p.block = false AND p.company.id = :companyId";

		if (productCategoryId != null) {
			queryString += " AND p.productCategory.id = :productCategoryId";
		}

		if (tokens.length != 0) {

			for (int i = 0; i < tokens.length - 1; i++) {
				queryString += " AND LOWER(p.name) LIKE LOWER(:token" + i + ")";
			}

			queryString += " AND LOWER(p.name) LIKE LOWER(:token" + (tokens.length - 1) + ")";

		}

		queryString += " ORDER BY p.productCategory.id";

		Query query = entityManager.createQuery(queryString);

		query.setParameter("companyId", companyId);

		if (productCategoryId != null) {
			query.setParameter("productCategoryId", productCategoryId);
		}

		if (tokens.length != 0) {
			for (int i = 0; i < tokens.length; i++) {
				query.setParameter("token" + i, '%' + tokens[i] + '%');
			}

		}

		List<Product> products = query.getResultList();

		return products;

	}

}
