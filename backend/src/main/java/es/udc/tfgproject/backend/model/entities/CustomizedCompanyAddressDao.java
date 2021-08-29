package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedCompanyAddressDao {

	Slice<CompanyAddress> find(Long companyCategoryId, Long cityId, String street, String keywords, int page, int size);

	Slice<CompanyAddress> findByKeywords(String keywords, int page, int size);

}
