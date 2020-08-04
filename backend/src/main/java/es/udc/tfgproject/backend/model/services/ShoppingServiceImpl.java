package es.udc.tfgproject.backend.model.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfgproject.backend.model.entities.Product;
import es.udc.tfgproject.backend.model.entities.ProductDao;
import es.udc.tfgproject.backend.model.entities.ShoppingCart;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItem;
import es.udc.tfgproject.backend.model.entities.ShoppingCartItemDao;
import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ShoppingCartItemDao shoppingCartItemDao;

	@Override
	public ShoppingCart addToShoppingCart(Long userId, Long shoppingCartId, Long productId, int quantity)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		Optional<Product> product = productDao.findById(productId);

		if (!product.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (existingCartItem.isPresent()) {
			existingCartItem.get().incrementQuantity(quantity);
		} else {
			ShoppingCartItem newCartItem = new ShoppingCartItem(product.get(), shoppingCart, quantity);
			shoppingCart.addItem(newCartItem);
			shoppingCartItemDao.save(newCartItem);
		}

		return shoppingCart;

	}

	@Override
	public ShoppingCart updateShoppingCartItemQuantity(Long userId, Long shoppingCartId, Long productId, int quantity)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (!existingCartItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		existingCartItem.get().setQuantity(quantity);

		return shoppingCart;

	}

	@Override
	public ShoppingCart removeShoppingCartItem(Long userId, Long shoppingCartId, Long productId)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);

		Optional<ShoppingCartItem> existingCartItem = shoppingCart.getItem(productId);

		if (!existingCartItem.isPresent()) {
			throw new InstanceNotFoundException("project.entities.product", productId);
		}

		shoppingCart.removeItem(existingCartItem.get());
		shoppingCartItemDao.delete(existingCartItem.get());

		return shoppingCart;

	}

	@Override
	@Transactional(readOnly = true)
	public ShoppingCart findShoppingCartProducts(Long userId, Long shoppingCartId, Long companyId)
			throws InstanceNotFoundException, PermissionException {

		ShoppingCart shoppingCart = permissionChecker.checkShoppingCartExistsAndBelongsToUser(shoppingCartId, userId);
		Set<ShoppingCartItem> items = new HashSet<>();
		items.addAll(shoppingCartItemDao.findByProductCompanyId(companyId));
		shoppingCart.setItems(items);

		return shoppingCart;

	}

}
