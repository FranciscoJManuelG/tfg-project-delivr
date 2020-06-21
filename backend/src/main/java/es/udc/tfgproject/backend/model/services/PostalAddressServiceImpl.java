package es.udc.tfgproject.backend.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.entities.Company;
import es.udc.tfgproject.backend.model.entities.CompanyAddress;
import es.udc.tfgproject.backend.model.entities.CompanyAddressDao;
import es.udc.tfgproject.backend.model.entities.CompanyDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;

@Service
@Transactional
public class PostalAddressServiceImpl implements PostalAddressService {

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAddressDao companyAddressDao;

	@Override
	public Address addAddress(String street, String cp, Long cityId, Long companyId) throws InstanceNotFoundException {

		City city = checkCity(cityId);
		Company company = checkCompany(companyId);

		Address address = addressDao.save(new Address(street, cp, city));

		companyAddressDao.save(new CompanyAddress(company, address));

		return address;
	}

	// DUDA : lo añado, o con añadir y eliminar es suficiente.
	// A lo que me refiero es que si quiero añadir una nueva dirección la añado
	// normal y si me he equivocado en alguna
	// puedo eliminarla y volver a añadirla

	/*
	 * @Override public Address modifyAddress(Long addressId, String street, String
	 * cp, Long cityId) throws InstanceNotFoundException {
	 * 
	 * City city = checkCity(cityId);
	 * 
	 * Optional<Address> addressOpt = addressDao.findById(addressId); Address
	 * address = null; if (!addressOpt.isPresent()) { throw new
	 * InstanceNotFoundException("project.entities.address", addressId); } else
	 * address = addressOpt.get();
	 * 
	 * address.setStreet(street); address.setCp(cp); address.setCity(city);
	 * 
	 * return address; }
	 */

	@Override
	public void deleteAddress(Long addressId) throws InstanceNotFoundException {

		companyAddressDao.delete(companyAddressDao.findByAddressId(addressId).get());

		addressDao.deleteById(addressId);
	}

	@Override
	public Block<Address> findAddresses(Long companyId, int page, int size) {

		Slice<Address> slice = addressDao.find(companyId, page, size);

		return new Block<>(slice.getContent(), slice.hasNext());

	}

	@Override
	public List<City> findAllCities() {

		Iterable<City> cities = cityDao.findAll();
		List<City> citiesList = new ArrayList<>();

		cities.forEach(city -> citiesList.add(city));

		return citiesList;

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

	/*
	 * Método privado que comprueba que la empresa está registrada en el sistema
	 */
	private Company checkCompany(Long companyId) throws InstanceNotFoundException {
		Optional<Company> companyOpt = companyDao.findById(companyId);
		Company company = null;

		if (!companyOpt.isPresent()) {
			throw new InstanceNotFoundException("project.entities.company", companyId);
		} else
			company = companyOpt.get();

		return company;
	}

}
