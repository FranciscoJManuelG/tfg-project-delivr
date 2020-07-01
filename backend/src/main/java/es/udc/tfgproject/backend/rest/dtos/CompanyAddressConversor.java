package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.CompanyAddress;

public class CompanyAddressConversor {

	private CompanyAddressConversor() {
	}

	public final static CompanyAddressDto toCompanyAddressDto(CompanyAddress companyAddress) {

		return new CompanyAddressDto(companyAddress.getAddressId(), companyAddress.getStreet(), companyAddress.getCp(),
				companyAddress.getCity().getId());
	}

	public final static List<CompanyAddressSummaryDto> toCompanyAddressSummaryDtos(
			List<CompanyAddress> companyAddresses) {
		return companyAddresses.stream().map(o -> toCompanyAddressSummaryDto(o)).collect(Collectors.toList());
	}

	private final static CompanyAddressSummaryDto toCompanyAddressSummaryDto(CompanyAddress companyAddress) {

		return new CompanyAddressSummaryDto(companyAddress.getAddressId(), companyAddress.getStreet(),
				companyAddress.getCp(), companyAddress.getCity().getId());

	}

}
