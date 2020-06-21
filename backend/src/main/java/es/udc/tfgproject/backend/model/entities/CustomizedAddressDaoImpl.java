package es.udc.tfgproject.backend.model.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class CustomizedAddressDaoImpl implements CustomizedAddressDao {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Slice<Address> find(Long companyId, int page, int size) {

		String queryString = "SELECT DISTINCT a FROM Company c JOIN CompanyAddress ca ON ca.company.id = :companyId"
				+ " JOIN Address a ON ca.address.id = a.id ORDER BY a.id";

		Query query = entityManager.createQuery(queryString).setFirstResult(page * size).setMaxResults(size + 1);

		if (companyId != null) {
			query.setParameter("companyId", companyId);
		}

		List<Address> addresses = query.getResultList();
		boolean hasNext = addresses.size() == (size + 1);

		if (hasNext) {
			addresses.remove(addresses.size() - 1);
		}

		return new SliceImpl<>(addresses, PageRequest.of(page, size), hasNext);

	}

}
