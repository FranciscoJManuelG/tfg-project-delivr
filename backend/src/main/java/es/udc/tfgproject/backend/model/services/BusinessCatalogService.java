package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.CompanyAddress;

public interface BusinessCatalogService {

	Block<CompanyAddress> findCompanies(Long companyCategoryId, Long cityId, String street, String keywords, int page,
			int size);

	Block<CompanyAddress> findAllCompanies(String keywords, int page, int size);

}
