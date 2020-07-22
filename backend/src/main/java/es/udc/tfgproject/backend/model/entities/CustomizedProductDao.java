package es.udc.tfgproject.backend.model.entities;

import org.springframework.data.domain.Slice;

public interface CustomizedProductDao {

	Slice<Product> find(Long productCategoryId, String keywords, int page, int size);

}
