package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedCompanyDao {

	Slice<Company> find(Long companyCategoryId, Long cityId, String street, String keywords, int page, int size);

}
