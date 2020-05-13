package es.udc.tfgproject.backend.test.model.services;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.tfgproject.backend.model.entities.Address;
import es.udc.tfgproject.backend.model.entities.AddressDao;
import es.udc.tfgproject.backend.model.entities.City;
import es.udc.tfgproject.backend.model.entities.CityDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.services.AddressService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AddressServiceTest {

	private final long NON_EXISTENT_ADDRESS_ID = new Long(-1);
	private final long NON_EXISTENT_CITY_ID = new Long(-1);

	@Autowired
	private AddressService addressService;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AddressDao addressDao;

	@Test
	public void testAddAddress() throws InstanceNotFoundException {
		City city = new City("A Coruña");
		cityDao.save(city);

		long numberOfAddresses = addressDao.count();

		addressService.addAddress("Rosalia 18", "15700", city.getId());

		assertEquals(numberOfAddresses + 1, addressDao.count());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistingCity() throws InstanceNotFoundException {

		addressService.addAddress("Rosalia 18", "15700", NON_EXISTENT_CITY_ID);
	}

	@Test
	public void testModifyAddress() throws InstanceNotFoundException {
		City city = new City("A Coruña");
		cityDao.save(city);

		Address address = addressService.addAddress("Rosalia 18", "15700", city.getId());

		Address modifiedAddress = addressService.modifyAddress(address.getId(), "Monelos 23", "15870", city.getId());

		assertEquals(modifiedAddress.getStreet(), "Monelos 23");
		assertEquals(modifiedAddress.getCp(), "15870");
		assertEquals(modifiedAddress.getCity().getId(), city.getId());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testModifyToNonExistingAddress() throws InstanceNotFoundException {

		City city = new City("A Coruña");
		cityDao.save(city);

		addressService.modifyAddress(NON_EXISTENT_ADDRESS_ID, "Monelos 23", "15870", city.getId());
	}

	@Test
	public void testDeleteAddress() throws InstanceNotFoundException {
		City city = new City("A Coruña");
		cityDao.save(city);

		Address address = addressService.addAddress("Rosalia 18", "15700", city.getId());

		long actualNumberOfAddresses = addressDao.count();

		addressService.deleteAddress(address.getId());

		assertEquals(actualNumberOfAddresses - 1, addressDao.count());

	}

}
