package es.udc.tfgproject.backend.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;

@Service
@Transactional(readOnly = true)
public class BusinessCatalogServiceImpl implements BusinessCatalogService {

	@Autowired
	private CompanyAddressDao companyAddressDao;

	@Override
	public Block<CompanyAddress> findCompanies(Long companyCategoryId, Long cityId, String street, String keywords,
			int page, int size) {

		Slice<CompanyAddress> slice = companyAddressDao.find(companyCategoryId, cityId, street, keywords, page, size);

		return new Block<>(slice.getContent(), slice.hasNext());

	}

}
