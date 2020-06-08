package es.udc.tfgproject.backend.rest.dtos;

import es.udc.tfgproject.backend.model.entities.Company;

public class CompanyConversor {

	private CompanyConversor() {
	}

	public final static CompanyDto toCompanyDto(Company company) {

		return new CompanyDto(company.getId(), company.getName(), company.getCapacity(), company.getReserve(),
				company.getHomeSale(), company.getReservePercentage(), company.getCompanyCategory().getId(),
				company.getUser().getUserName());
	}

}
