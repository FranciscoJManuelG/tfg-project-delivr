package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.AddressConversor.toAddressDto;
import static es.udc.tfgproject.backend.rest.dtos.AddressConversor.toAddressSummaryDtos;
import static es.udc.tfgproject.backend.rest.dtos.CityConversor.toCityDtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.Block;
import es.udc.tfgproject.backend.model.services.PostalAddressService;
import es.udc.tfgproject.backend.rest.dtos.AddAddressParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddressDto;
import es.udc.tfgproject.backend.rest.dtos.AddressSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.BlockDto;
import es.udc.tfgproject.backend.rest.dtos.CityDto;

@RestController
@RequestMapping("/postalAddresses")
public class PostalAddressController {

	@Autowired
	private PostalAddressService postalAddressService;

	@PostMapping("/{companyId}/addAddress")
	public AddressDto addAddress(@PathVariable Long companyId, @Validated @RequestBody AddAddressParamsDto params)
			throws InstanceNotFoundException {

		return toAddressDto(
				postalAddressService.addAddress(params.getStreet(), params.getCp(), params.getCityId(), companyId));

	}

	@DeleteMapping("/{addressId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAddress(@PathVariable Long addressId) throws InstanceNotFoundException {

		postalAddressService.deleteAddress(addressId);

	}

	@GetMapping("/{companyId}/addresses")
	public BlockDto<AddressSummaryDto> findAddresses(@PathVariable Long companyId,
			@RequestParam(defaultValue = "0") int page) {

		Block<Address> addressBlock = postalAddressService.findAddresses(companyId, page, 10);

		return new BlockDto<>(toAddressSummaryDtos(addressBlock.getItems()), addressBlock.getExistMoreItems());

	}

	@GetMapping("/cities")
	public List<CityDto> findAllCities() {
		return toCityDtos(postalAddressService.findAllCities());
	}

}
