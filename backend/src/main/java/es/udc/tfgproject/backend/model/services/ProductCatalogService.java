package es.udc.tfgproject.backend.model.services;

import java.util.List;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;

public interface ProductCatalogService {

	List<Product> findProducts(Long companyId, Long productCategoryId, String keywords);

	List<ProductCategory> findCompanyProductCategories(Long companyId);
}
