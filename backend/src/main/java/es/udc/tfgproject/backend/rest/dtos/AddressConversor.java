package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.Address;

public class AddressConversor {

	private AddressConversor() {
	}

	public final static AddressDto toAddressDto(Address address) {

		return new AddressDto(address.getId(), address.getStreet(), address.getCp(), address.getCity().getId());
	}

	public final static List<AddressSummaryDto> toAddressSummaryDtos(List<Address> addresses) {
		return addresses.stream().map(o -> toAddressSummaryDto(o)).collect(Collectors.toList());
	}

	private final static AddressSummaryDto toAddressSummaryDto(Address address) {

		return new AddressSummaryDto(address.getId(), address.getStreet(), address.getCp(), address.getCity().getId());

	}

}
