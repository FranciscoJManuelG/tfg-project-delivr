package es.udc.tfgproject.backend.rest.controllers;

import static es.udc.tfgproject.backend.rest.dtos.ProductCategoryConversor.toProductCategoryDto;
import static es.udc.tfgproject.backend.rest.dtos.ProductCategoryConversor.toProductCategoryDtos;
import static es.udc.tfgproject.backend.rest.dtos.ProductConversor.toProductDto;
import static es.udc.tfgproject.backend.rest.dtos.ProductConversor.toProductSummaryDtos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.udc.tfgproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.tfgproject.backend.model.exceptions.PermissionException;
import es.udc.tfgproject.backend.model.services.ProductManagementService;
import es.udc.tfgproject.backend.rest.dtos.AddProductCategoryParamsDto;
import es.udc.tfgproject.backend.rest.dtos.AddProductParamsDto;
import es.udc.tfgproject.backend.rest.dtos.EditProductParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ModifyProductCategoryParamsDto;
import es.udc.tfgproject.backend.rest.dtos.ProductCategoryDto;
import es.udc.tfgproject.backend.rest.dtos.ProductDto;
import es.udc.tfgproject.backend.rest.dtos.ProductSummaryDto;
import es.udc.tfgproject.backend.rest.dtos.StateProductParamsDto;

@RestController
@RequestMapping("/management")
public class ProductManagementController {

	@Autowired
	private ProductManagementService productManagementService;

	@CrossOrigin
	@PostMapping(value = "/products", consumes = { "multipart/form-data" })
	public ProductDto addProduct(@RequestAttribute Long userId,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("data") AddProductParamsDto params)
			throws InstanceNotFoundException, PermissionException, IOException {

		return toProductDto(productManagementService.addProduct(userId, params.getCompanyId(), params.getName(),
				params.getDescription(), params.getPrice(), file != null ? createFile(file) : null,
				params.getProductCategoryId()));
	}

	@CrossOrigin
	@PutMapping(value = "/products/{productId}", consumes = { "multipart/form-data" })
	public ProductDto editProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("data") EditProductParamsDto params)
			throws InstanceNotFoundException, PermissionException, IOException {

		return toProductDto(productManagementService.editProduct(userId, params.getCompanyId(), productId,
				params.getName(), params.getDescription(), params.getPrice(), file != null ? createFile(file) : null,
				params.getProductCategoryId()));

	}

	@DeleteMapping("/products/{productId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@RequestParam(required = true) Long companyId) throws InstanceNotFoundException, PermissionException {

		productManagementService.removeProduct(userId, companyId, productId);

	}

	@PostMapping("/products/{productId}/block")
	public ProductDto blockProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@Validated @RequestBody StateProductParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.blockProduct(userId, params.getCompanyId(), productId));

	}

	@PostMapping("/products/{productId}/unlock")
	public ProductDto unlockProduct(@RequestAttribute Long userId, @PathVariable Long productId,
			@Validated @RequestBody StateProductParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductDto(productManagementService.unlockProduct(userId, params.getCompanyId(), productId));

	}

	@GetMapping("/products/{productId}")
	public ProductDto findProduct(@RequestAttribute Long userId, @PathVariable Long productId)
			throws InstanceNotFoundException, PermissionException {
		return toProductDto(productManagementService.findProduct(userId, productId));
	}

	@GetMapping("/products/categories")
	public List<ProductCategoryDto> findAllProductCategories() {
		return toProductCategoryDtos(productManagementService.findAllProductCategories());
	}

	@PostMapping("/productCategories")
	public ProductCategoryDto addProductCategory(@RequestAttribute Long userId,
			@Validated @RequestBody AddProductCategoryParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductCategoryDto(productManagementService.addProductCategory(userId, params.getName()));

	}

	@PutMapping("/productCategories/{productCategoryId}")
	public ProductCategoryDto modifyProductCategory(@RequestAttribute Long userId, @PathVariable Long productCategoryId,
			@Validated @RequestBody ModifyProductCategoryParamsDto params)
			throws InstanceNotFoundException, PermissionException {

		return toProductCategoryDto(
				productManagementService.modifyProductCategory(userId, productCategoryId, params.getName()));

	}

	@GetMapping("/products")
	public List<ProductSummaryDto> findAllCompanyProducts(@RequestAttribute Long userId, @RequestParam Long companyId)
			throws InstanceNotFoundException, PermissionException {
		return toProductSummaryDtos(productManagementService.findAllCompanyProducts(userId, companyId));
	}

	private String createFile(MultipartFile file) throws IOException {
		Path path = Paths.get(File.separator + "home" + File.separator + "fran" + File.separator + "software"
				+ File.separator + "tfg-project-delivr" + File.separator + "frontend" + File.separator + "public"
				+ File.separator + "img" + File.separator + getTodayDate() + "_" + file.getOriginalFilename());

		try {

			byte[] bytes = file.getBytes();
			Files.write(path, bytes);

			return path.getFileName().toString();
		} catch (IOException ex) {

			throw new RuntimeException("Internal error img");
		}

	}

	private String getTodayDate() {

		Date date = new Date();
		String dateStr = "YYYYMMddhhmmss";
		SimpleDateFormat format = new SimpleDateFormat(dateStr);
		return format.format(date);

	}

}
