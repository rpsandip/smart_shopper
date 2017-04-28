package com.chillies.smartshopper.lib.transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DirectoryFiles;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.service.IWebAdminDbService;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * ProductTransaction : is for database transaction for @Sudoers
 * 
 * @author Jinen Kothari
 *
 */
@Service
public final class ProductTransaction {

	private static final Logger LOG = LoggerFactory.getLogger(ProductTransaction.class);

	@Autowired
	private IWebAdminDbService dbService;

	private ProductCategory save(final ProductCategory productCategory) {
		Preconditions.checkNotNull(productCategory, "productCategory can not be null.");

		return dbService.save(productCategory);
	}

	public Set<ProductCategory> categories() {
		return dbService.productCategories();
	}

	public void isProductCategoryName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<ProductCategory> optionalProductCategory = dbService.byProductCategory(name);
		if (optionalProductCategory.isPresent()) {
			LOG.info(String.format("isProductCategoryName() category %s already exists.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_ALREADY_EXISTS);
		}
	}

	public Product save(final Product product) {
		Preconditions.checkNotNull(product, "product can not be null.");

		return dbService.save(product);
	}

	public Set<Product> products() {
		return dbService.products();
	}

	public Set<Product> products(final ProductCategory category) {
		return dbService.products(category);
	}

	public void isProductName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<Product> optional = dbService.byProduct(name);
		if (optional.isPresent()) {
			LOG.info(String.format("isProductName() %s already exists.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_NAME_ALREADY_EXISTS);
		}
	}

	public ProductCategory getProductCategortByName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<ProductCategory> optionalProductCategory = dbService.byProductCategoryById(name);

		if (!optionalProductCategory.isPresent()) {
			LOG.info(String.format("getProductCategortByName() category %s is not present.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_IS_NOT_PRESENT);
		}

		return optionalProductCategory.get();
	}

	public ProductCategory getProductCategoryId(final String categortId) {
		Preconditions.checkNotNull(categortId, "categortId can not be null.");

		final Optional<ProductCategory> optionalProductCategory = dbService.byProductCategoryById(categortId);

		if (!optionalProductCategory.isPresent()) {
			LOG.info(String.format("getProductCategoryId() category %s is not present.", categortId));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_IS_NOT_PRESENT);
		}

		return optionalProductCategory.get();
	}

	public ProductCategory save(final String name, final Optional<String> remark, final DateMeta dateMeta,
			final CreatedMeta createdMeta) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");

		final ProductCategory productCategory = new ProductCategory(name, remark, dateMeta, createdMeta);

		return this.save(productCategory);
	}

	public SortedSet<ProductCategoryShell> sortedCategories() {
		final SortedSet<ProductCategoryShell> shells = new TreeSet<>(Comparator.comparing(ProductCategoryShell::getId))
				.descendingSet();
		final Set<ProductCategory> categories = this.categories();
		categories.forEach(category -> shells.add(category.toShell()));
		return shells;
	}

	public SortedSet<ProductShell> sortedProducts(final String baseURL) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be empty.");
		final SortedSet<ProductShell> shells = new TreeSet<>(Comparator.comparing(ProductShell::getId)).descendingSet();
		final Set<Product> products = this.products();
		products.forEach(product -> shells.add(product.toShell(baseURL)));
		return shells;
	}

	public SortedSet<ProductShell> sortedCategorizedProduct(final String baseURL, final ProductCategory category) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be empty.");
		Preconditions.checkNotNull(category, "category can not be empty.");
		final SortedSet<ProductShell> shells = new TreeSet<>(Comparator.comparing(ProductShell::getId)).descendingSet();
		final Set<Product> products = this.products(category);
		products.forEach(product -> shells.add(product.toShell(baseURL)));
		return shells;
	}

	public Product save(final String name, final Optional<String> optionalRemark, final double price,
			final double points, final ProductCategory productCategory, final Sudoers sudoers,
			final Optional<String> path) {
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(optionalRemark, "optionalRemark can not be null.");
		Preconditions.checkNotNull(productCategory, "productCategory can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(path, "path can not be null.");
		final Product product = new Product(name, optionalRemark, new DateMeta(Optional.absent()),
				new CreatedMeta(sudoers, Optional.absent()), price, points, productCategory, path);

		return this.save(product);
	}

	public String uploadImageToDisk(final MultipartFile file, final String name) {
		Preconditions.checkNotNull(file, "file can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");

		if (!file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
			final String error = MessageUtils.IMAGE_TYPE;
			throw new NotAccatable(error);
		}

		if (file.getSize() > 400000) {
			final String error = MessageUtils.IMAGE_SIZE;
			throw new NotAccatable(error);
		}

		final String path = DirectoryFiles.FILES.getCompletePath() + AppUtils.PATH_SEPARATOR + name + ".png";

		try {
			file.transferTo(new File(path));
			return path;
		} catch (IllegalStateException | IOException e) {
			final String error = String.format("addOrUpdateItem() Failed with Message:%s", e.getMessage());
			LOG.warn(error);
			throw new NotAccatable(error);
		}
	}

	public Product getProductId(final String productId) {
		Preconditions.checkNotNull(productId, "productId can not be null.");

		final Optional<Product> optional = dbService.byProductId(productId);

		if (!optional.isPresent()) {
			LOG.info(String.format("getProductId() product %s is not present.", productId));
			throw new NotAccatable(MessageUtils.PRODUCT_IS_NOT_PRESENT);
		}

		return optional.get();
	}

	public InputStream getProductImage(final String productCode) throws IOException {

		final String imagePath = DirectoryFiles.FILES.getCompletePath() + AppUtils.PATH_SEPARATOR + productCode
				+ ".png";

		if (!Files.exists(Paths.get(imagePath), LinkOption.NOFOLLOW_LINKS)) {
			throw new NotAccatable(MessageUtils.fileNotExists(productCode));
		}
		InputStream inputStream = new FileInputStream(new File(imagePath));
		return inputStream;
	}

}
