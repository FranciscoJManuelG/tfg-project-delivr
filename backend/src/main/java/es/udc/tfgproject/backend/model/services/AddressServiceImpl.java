package es.udc.tfgproject.backend.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AddressDao addressDao;

	@Override
	public Address addAddress(String street, String cp, Long cityId) throws InstanceNotFoundException {

		City city = checkCity(cityId);

		Address address = addressDao.save(new Address(street, cp, city));

		return address;
	}

	@Override
	public Address modifyAddress(Long addressId, String street, String cp, Long cityId)
			throws InstanceNotFoundException {

		City city = checkCity(cityId);

		Optional<Address> addressOpt = addressDao.findById(addressId);
		Address address = null;
		if (!addressOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.address", addressId);
		} else
			address = addressOpt.get();

		address.setStreet(street);
		address.setCp(cp);
		address.setCity(city);

		return address;
	}

	@Override
	public void deleteAddress(Long addressId) throws InstanceNotFoundException {

		addressDao.deleteById(addressId);
	}

	/*
	 * Método privado que comprueba que la ciudad está registrada en el sistema
	 */
	private City checkCity(Long cityId) throws InstanceNotFoundException {
		Optional<City> cityOpt = cityDao.findById(cityId);
		City city = null;

		if (!cityOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.city", cityId);
		} else
			city = cityOpt.get();

		return city;
	}

}
