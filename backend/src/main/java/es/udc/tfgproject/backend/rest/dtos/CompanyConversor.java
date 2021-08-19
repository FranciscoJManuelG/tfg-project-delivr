package es.udc.tfgproject.backend.rest.dtos;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.services.Constantes;

public class CompanyConversor {

	private CompanyConversor() {
	}

	public final static CompanyDto toCompanyDto(Company company) {

		return new CompanyDto(company.getId(), company.getName(), company.getCapacity(), company.getReserve(),
				company.getHomeSale(), company.getReservePercentage(), company.getBlock(),
				company.getUser().getUserName(), company.getCompanyCategory().getId(), company.getOpeningTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				company.getClosingTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				company.getLunchTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				company.getDinerTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)));
	}

	public final static List<CompanySummaryDto> toCompanySummaryDtos(List<CompanyAddress> companyAddresses) {
		return companyAddresses.stream().map(o -> toCompanySummaryDto(o)).collect(Collectors.toList());
	}

	private final static CompanySummaryDto toCompanySummaryDto(CompanyAddress companyAddress) {

		return new CompanySummaryDto(companyAddress.getCompany().getId(), companyAddress.getStreet(),
				companyAddress.getCp(), companyAddress.getCity().getId(), companyAddress.getCompany().getName(),
				companyAddress.getCompany().getCapacity(), companyAddress.getCompany().getReserve(),
				companyAddress.getCompany().getHomeSale(), companyAddress.getCompany().getReservePercentage(),
				companyAddress.getCompany().getCompanyCategory().getId(), companyAddress.getCompany().getOpeningTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				companyAddress.getCompany().getClosingTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				companyAddress.getCompany().getLunchTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)),
				companyAddress.getCompany().getDinerTime().format(DateTimeFormatter.ofPattern(Constantes.HH_MM)));

	}

}
