package es.udc.tfgproject.backend.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyDao;

@Service
@Transactional
public class BusinessCatalogServiceImpl implements BusinessCatalogService {

	@Autowired
	private CompanyDao companyDao;

	@Override
	public Block<Company> findCompanies(Long companyCategoryId, Long cityId, String street, String keywords, int page,
			int size) {

		Slice<Company> slice = companyDao.find(companyCategoryId, cityId, street, keywords, page, size);

		return new Block<>(slice.getContent(), slice.hasNext());

	}

}
