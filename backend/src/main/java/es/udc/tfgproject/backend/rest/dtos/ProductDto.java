package es.udc.tfgproject.backend.rest.dtos;

import java.math.BigDecimal;

public class ProductDto {

	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Boolean block;
	private String path;
	private Long companyId;
	private Long productCategoryId;

	public ProductDto() {
	}

	public ProductDto(Long id, String name, String description, BigDecimal price, Boolean block, String path,
			Long companyId, Long productCategoryId) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.block = block;
		this.path = path;
		this.companyId = companyId;
		this.productCategoryId = productCategoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Boolean getBlock() {
		return block;
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

}
