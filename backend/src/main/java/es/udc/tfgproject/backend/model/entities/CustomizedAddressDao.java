package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedAddressDao {

	Slice<Address> find(Long companyId, int page, int size);

}
