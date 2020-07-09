package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Company;

public interface BusinessCatalogService {

	Block<Company> findCompanies(Long companyCategoryId, Long cityId, String street, String keywords, int page,
			int size);

}
