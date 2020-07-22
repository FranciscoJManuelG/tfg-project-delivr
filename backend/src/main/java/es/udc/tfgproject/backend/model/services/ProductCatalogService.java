package es.udc.tfgproject.backend.model.services;

import java.util.List;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;

public interface ProductCatalogService {

	Block<Product> findProducts(Long productCategoryId, String keywords, int page, int size);

	List<ProductCategory> findCompanyProductCategories(Long companyId);
}
