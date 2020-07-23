package es.udc.tfgproject.backend.model.entities;

import java.util.List;

public interface CustomizedProductDao {

	List<Product> find(Long companyId, Long productCategoryId, String keywords);

}
