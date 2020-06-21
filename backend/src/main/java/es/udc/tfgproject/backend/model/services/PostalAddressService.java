package es.udc.tfgproject.backend.model.services;

import java.util.List;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

public interface PostalAddressService {

	Address addAddress(String street, String cp, Long cityId, Long companyId) throws InstanceNotFoundException;

	// Address modifyAddress(Long addressId, String street, String cp, Long cityId)
	// throws InstanceNotFoundException;

	void deleteAddress(Long addressId) throws InstanceNotFoundException;

	Block<Address> findAddresses(Long companyId, int page, int size);

	List<City> findAllCities();

}
