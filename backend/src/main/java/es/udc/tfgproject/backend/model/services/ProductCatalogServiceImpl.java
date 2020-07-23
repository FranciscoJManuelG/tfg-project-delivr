package es.udc.tfgproject.backend.model.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductCategory;
import es.udc.tfgproject.backend.model.entities.ProductDao;

@Service
@Transactional(readOnly = true)
public class ProductCatalogServiceImpl implements ProductCatalogService {

	@Autowired
	private ProductDao productDao;

	@Override
	public List<Product> findProducts(Long companyId, Long productCategoryId, String keywords) {

		return productDao.find(companyId, productCategoryId, keywords);

	}

	@Override
	public List<ProductCategory> findCompanyProductCategories(Long companyId) {
		List<Product> products = productDao.findByCompanyId(companyId);
		List<ProductCategory> categoriesList = new ArrayList<>();

		for (Product product : products) {
			if (!categoriesList.contains(product.getProductCategory())) {
				categoriesList.add(product.getProductCategory());
			}
		}

		return categoriesList;
	}

}
