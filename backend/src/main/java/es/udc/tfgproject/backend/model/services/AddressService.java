package es.udc.tfgproject.backend.model.services;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

public interface AddressService {

	Address addAddress(String street, String cp, Long cityId, Long companyId) throws InstanceNotFoundException;

	// Address modifyAddress(Long addressId, String street, String cp, Long cityId)
	// throws InstanceNotFoundException;

	void deleteAddress(Long addressId) throws InstanceNotFoundException;

	Block<Address> findAllCompanyAddress(Long companyId, int page, int size);

}
