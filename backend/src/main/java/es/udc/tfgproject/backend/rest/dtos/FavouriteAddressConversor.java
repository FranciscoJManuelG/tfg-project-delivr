package es.udc.tfgproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.tfgproject.backend.model.entities.FavouriteAddress;

public class FavouriteAddressConversor {

	private FavouriteAddressConversor() {
	}

	public final static FavouriteAddressDto toFavouriteAddressDto(FavouriteAddress favouriteAddress) {

		return new FavouriteAddressDto(favouriteAddress.getAddressId(), favouriteAddress.getStreet(),
				favouriteAddress.getCp(), favouriteAddress.getCity().getId());
	}

	public final static List<FavouriteAddressSummaryDto> toFavouriteAddressSummaryDtos(
			List<FavouriteAddress> favouriteAddresses) {
		return favouriteAddresses.stream().map(o -> toFavouriteAddressSummaryDto(o)).collect(Collectors.toList());
	}

	private final static FavouriteAddressSummaryDto toFavouriteAddressSummaryDto(FavouriteAddress favouriteAddress) {

		return new FavouriteAddressSummaryDto(favouriteAddress.getAddressId(), favouriteAddress.getStreet(),
				favouriteAddress.getCp(), favouriteAddress.getCity().getId());

	}

}
