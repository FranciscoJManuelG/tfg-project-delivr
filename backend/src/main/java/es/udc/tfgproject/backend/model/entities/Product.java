package es.udc.tfgproject.backend.model.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Product {

	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Boolean block;
	private ProductCategory productCategory;
	private Company company;
	private Image image;

	public Product() {
	}

	public Product(String name, String description, BigDecimal price, Boolean block, ProductCategory productCategory,
			Company company, Image image) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.block = block;
		this.productCategory = productCategory;
		this.company = company;
		this.image = image;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "productCategoryId")
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "imageId")
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
