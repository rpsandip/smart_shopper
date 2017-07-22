package com.chillies.smartshopper.service.transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chillies.smartshopper.common.shell.web.OrderShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.CartStatus;
import com.chillies.smartshopper.common.util.DirectoryFiles;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.common.util.OrderStatus;
import com.chillies.smartshopper.common.util.SuperCategory;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.ProductMeta;
import com.chillies.smartshopper.lib.model.web.Cart;
import com.chillies.smartshopper.lib.model.web.Order;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.service.IWebAdminDbService;
import com.chillies.smartshopper.lib.service.IWebDbService;
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
	private IWebAdminDbService iWebAdminDbService;

	@Autowired
	private IWebDbService iWebDbService;

	@Autowired
	private SudoersTransaction sudoersTransaction;

	@Autowired
	private UsersTransactions usersTransactions;

	@PostConstruct
	private void init() {
		final Set<ProductCategory> categories = iWebAdminDbService.productCategories();

		if (categories != null) {
			categories.forEach(category -> {
				if (category.getSuperCategory() == null) {
					category.update(SuperCategory.GROCERY, category.getName(),
							Optional.fromNullable(category.getRemark()), category.getCreatedMeta().getCreated());
					iWebAdminDbService.save(category);
				}
			});
		}

		final Set<Product> products = iWebAdminDbService.products();
		if (products != null) {
			products.forEach(product -> {
				if (!product.isDeleted()) {
					product.migrate();
					iWebAdminDbService.save(product);
				}
			});
		}

	}

	private ProductCategory save(final ProductCategory productCategory) {
		Preconditions.checkNotNull(productCategory, "productCategory can not be null.");

		return iWebAdminDbService.save(productCategory);
	}

	private Cart save(final Cart cart) {
		Preconditions.checkNotNull(cart, "cart can not be null.");

		return iWebDbService.save(cart);
	}

	private Order save(final Order order) {
		Preconditions.checkNotNull(order, "order can not be null.");
		return iWebDbService.save(order);
	}

	private double totalSumOfItems(final Set<ProductMeta> metas) {
		Preconditions.checkNotNull(metas, "metas can not be null.");
		double total = 0;
		for (final ProductMeta product : metas) {
			double price = product.getQuantity() * product.price();
			total += price;
		}
		return total;
	}

	private double totalSumOfPoints(final Set<ProductMeta> metas) {
		Preconditions.checkNotNull(metas, "metas can not be null.");
		double total = 0;
		for (final ProductMeta product : metas) {
			double points = product.getQuantity() * product.getProduct().points();
			total += points;
		}
		return total;
	}

	public Optional<Order> orderById(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");

		return Optional.fromNullable(iWebDbService.byId(id));
	}

	public Order getOrderById(final String id) {
		Preconditions.checkNotNull(id, "id can not be null.");

		final Optional<Order> optional = this.orderById(id);
		if (!optional.isPresent()) {
			throw new NotAccatable(MessageUtils.ORDER_NOT_PRESENT);
		}
		return optional.get();
	}

	public Set<ProductCategory> categories() {
		return iWebAdminDbService.productCategories();
	}

	public void isProductCategoryName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<ProductCategory> optionalProductCategory = iWebAdminDbService.byProductCategory(name);
		if (optionalProductCategory.isPresent()) {
			LOG.info(String.format("isProductCategoryName() category %s already exists.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_ALREADY_EXISTS);
		}
	}

	public Product save(final Product product) {
		Preconditions.checkNotNull(product, "product can not be null.");

		return iWebAdminDbService.save(product);
	}

	public Set<Product> products() {
		return iWebAdminDbService.products();
	}

	public Set<Product> products(final String searchString) {
		Preconditions.checkNotNull(searchString, "searchString can not be null");
		return iWebAdminDbService.products(searchString);
	}

	public Set<Product> products(final ProductCategory category) {
		return iWebAdminDbService.products(category);
	}

	public void isProductName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<Product> optional = iWebAdminDbService.byProduct(name);
		if (optional.isPresent()) {
			LOG.info(String.format("isProductName() %s already exists.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_NAME_ALREADY_EXISTS);
		}
	}

	public ProductCategory getProductCategortByName(final String name) {
		Preconditions.checkNotNull(name, "name can not be null.");

		final Optional<ProductCategory> optionalProductCategory = iWebAdminDbService.byProductCategoryById(name);

		if (!optionalProductCategory.isPresent()) {
			LOG.info(String.format("getProductCategortByName() category %s is not present.", name));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_IS_NOT_PRESENT);
		}

		return optionalProductCategory.get();
	}

	public ProductCategory getProductCategoryId(final String categortId) {
		Preconditions.checkNotNull(categortId, "categortId can not be null.");

		final Optional<ProductCategory> optionalProductCategory = iWebAdminDbService.byProductCategoryById(categortId);

		if (!optionalProductCategory.isPresent()) {
			LOG.info(String.format("getProductCategoryId() category %s is not present.", categortId));
			throw new NotAccatable(MessageUtils.PRODUCT_CATEGORY_IS_NOT_PRESENT);
		}

		return optionalProductCategory.get();
	}

	public ProductCategory save(final SuperCategory superCategory, final String name, final Optional<String> remark,
			final DateMeta dateMeta, final CreatedMeta createdMeta) {
		Preconditions.checkNotNull(superCategory, "superCategory can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(dateMeta, "dateMeta can not be null.");
		Preconditions.checkNotNull(createdMeta, "createdMeta can not be null.");

		final ProductCategory productCategory = new ProductCategory(superCategory, name, remark, dateMeta, createdMeta);

		return this.save(productCategory);
	}

	public ProductCategory update(final SuperCategory superCategory, final ProductCategory category, final String name,
			final Optional<String> remark, final Sudoers sudoers) {
		Preconditions.checkNotNull(superCategory, "superCategory can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(remark, "remark can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");

		category.update(superCategory, name, remark, sudoers);
		return this.save(category);
	}

	public SortedSet<ProductCategoryShell> sortedCategories() {
		final SortedSet<ProductCategoryShell> shells = new TreeSet<>(
				Comparator.comparing(ProductCategoryShell::getName));
		final Set<ProductCategory> categories = this.categories();
		categories.forEach(category -> {
			if (!category.isDeleted()) {
				shells.add(category.toShell());
			}
		});
		return shells;
	}

	public SortedSet<ProductShell> sortedProducts(final String baseURL) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be empty.");
		final SortedSet<ProductShell> shells = new TreeSet<>(Comparator.comparing(ProductShell::getName));
		final Set<Product> products = this.products();
		products.forEach(product -> shells.add(product.toShell(baseURL)));
		return shells;
	}

	public SortedSet<ProductShell> sortedCategorizedProduct(final String baseURL, final ProductCategory category) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be empty.");
		Preconditions.checkNotNull(category, "category can not be empty.");
		final SortedSet<ProductShell> shells = new TreeSet<>(Comparator.comparing(ProductShell::getName));
		final Set<Product> products = this.products(category);
		products.forEach(product -> {
			if (!product.isDeleted()) {
				shells.add(product.toShell(baseURL));
			}
		});
		return shells;
	}

	public SortedSet<ProductShell> sortedCategorizedProduct(final String baseURL, final ProductCategory category,
			boolean dahsboard) {
		Preconditions.checkNotNull(baseURL, "baseURL can not be empty.");
		Preconditions.checkNotNull(category, "category can not be empty.");
		final SortedSet<ProductShell> shells = new TreeSet<>(Comparator.comparing(ProductShell::getName));
		final Set<Product> products = this.products(category);
		int i = 0;
		for (final Product product : products) {
			i++;

			if (!product.isDeleted()) {
				shells.add(product.toShell(baseURL));
				if (i == 4) {
					break;
				}
			}
		}
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

	public Product update(final Product oldProduct, final String name, final Optional<String> optionalRemark,
			double price, double points, final ProductCategory productCategory, final Sudoers sudoers) {
		Preconditions.checkNotNull(oldProduct, "oldProduct can not be null.");
		Preconditions.checkNotNull(name, "name can not be null.");
		Preconditions.checkNotNull(optionalRemark, "optionalRemark can not be null.");
		Preconditions.checkNotNull(productCategory, "productCategory can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		oldProduct.update(name, optionalRemark, price, points, productCategory, sudoers);
		return this.save(oldProduct);
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

	public Product getProductById(final String productId) {
		Preconditions.checkNotNull(productId, "productId can not be null.");

		final Optional<Product> optional = iWebAdminDbService.byProductId(productId);

		if (!optional.isPresent()) {
			LOG.info(String.format("getProductId() product %s is not present.", productId));
			throw new NotAccatable(MessageUtils.PRODUCT_IS_NOT_PRESENT);
		}

		return optional.get();
	}

	public InputStream getProductImage(final String productCode) throws IOException {
		Preconditions.checkNotNull(productCode, "productCode can not be null.");

		final String imagePath = DirectoryFiles.FILES.getCompletePath() + AppUtils.PATH_SEPARATOR + productCode
				+ ".png";

		if (!Files.exists(Paths.get(imagePath), LinkOption.NOFOLLOW_LINKS)) {
			throw new NotAccatable(MessageUtils.fileNotExists(productCode));
		}
		InputStream inputStream = new FileInputStream(new File(imagePath));
		return inputStream;
	}

	public Optional<Cart> getCartByUser(final Users users) {
		Preconditions.checkNotNull(users, "users can not be null.");

		return Optional.fromNullable(iWebDbService.byUsername(users, CartStatus.SAVED));
	}

	public Cart addOrUpdateCart(final Users users, final Product product, final int quantity) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(product, "product can not be null.");

		final Optional<Cart> optionalCart = this.getCartByUser(users);

		final Cart cart;
		if (optionalCart.isPresent()) {

			final Cart updateCart = optionalCart.get();
			final Optional<ProductMeta> optional = updateCart.getProducts(product.getId());
			if (optional.isPresent()) {
				final ProductMeta productMeta = optional.get();
				final int finalQuantity = productMeta.getQuantity() + quantity;
				productMeta.update(finalQuantity, product.price(), 0.0);
			} else {
				updateCart.addProduct(new ProductMeta(product, quantity, product.price(), 0.0));
			}
			cart = this.save(updateCart);

		} else {
			final Set<ProductMeta> productMetas = new HashSet<>();
			productMetas.add(new ProductMeta(product, quantity, product.price(), 0.0));
			cart = this.save(new Cart(new DateMeta(Optional.absent()), CartStatus.SAVED, users, productMetas));
		}
		return cart;
	}

	public Cart removeProductFromCart(final Cart cart, final Product product) {
		Preconditions.checkNotNull(cart, "cart can not be null.");
		Preconditions.checkNotNull(product, "product can not be null.");

		final Optional<ProductMeta> optional = cart.getProducts(product.getId());
		if (!optional.isPresent()) {
			throw new NotAccatable(MessageUtils.PRODUCT_IS_NOT_PRESENT);
		}
		cart.removeProductMeta(optional.get());
		return this.save(cart);
	}

	public Cart checkStatus(final Cart cart) {
		Preconditions.checkNotNull(cart, "cart can not be null.");

		cart.checkedOutStatus();
		return this.save(cart);
	}

	public Order placeOrder(final Cart cart, final Users users) {
		Preconditions.checkNotNull(cart, "cart can not be null.");
		Preconditions.checkNotNull(users, "users can not be null.");

		final double price = this.totalSumOfItems(cart.getProducts());
		final Sudoers sudoers = sudoersTransaction.getSudoers("sys");
		final Order order = this.save(new Order(new DateMeta(Optional.absent()), users, OrderStatus.PLACESED, price,
				0.0, 0.0, new CreatedMeta(sudoers, Optional.absent()), cart));

		return order;
	}

	public SortedSet<OrderShell> sortedOrders(final Users users, final String baseURL) {
		Preconditions.checkNotNull(users, "users can not be null.");
		final SortedSet<OrderShell> shells = new TreeSet<>(Comparator.comparing(OrderShell::getCreatedOn))
				.descendingSet();
		final Set<Order> orders = iWebDbService.orders(users);
		orders.forEach(order -> shells.add(order.toShell(baseURL)));
		return shells;
	}

	public SortedSet<OrderShell> sortedOrders(final String siteBaseUrl) {
		Preconditions.checkNotNull(siteBaseUrl, "siteBaseUrl can not be null.");
		final SortedSet<OrderShell> shells = new TreeSet<>(Comparator.comparing(OrderShell::getCreatedOn))
				.descendingSet();
		final Set<Order> orders = iWebDbService.orders();
		orders.forEach(order -> shells.add(order.toShell(siteBaseUrl)));
		return shells;
	}

	public Order cancelOrder(final Order order) {
		Preconditions.checkNotNull(order, "order can not be null");
		order.cancel();

		return this.save(order);
	}

	public Order orderStatusUpdate(final Order order, final OrderStatus status, final Sudoers sudoers) {
		Preconditions.checkNotNull(order, "order can not be null");
		Preconditions.checkNotNull(status, "status can not be null");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null");
		order.status(status, sudoers);
		if (status == OrderStatus.DELIVERED) {
			final Users users = usersTransactions.addPoints(order.getUsers(),
					this.totalSumOfPoints(order.getCart().getProducts()));
			final Optional<Users> optional = usersTransactions.bySubUser(users);
			if (optional.isPresent()) {
				usersTransactions.addPointsToParent(optional.get(),
						this.totalSumOfPoints(order.getCart().getProducts()));
			}
		}
		return this.save(order);
	}

	public SortedSet<ProductShell> search(final String searchString, final String baseURL) {
		Preconditions.checkNotNull(searchString, "searchString can not be null");
		Preconditions.checkNotNull(baseURL, "baseURL can not be null");

		final SortedSet<ProductShell> productShells = new TreeSet<>(Comparator.comparing(ProductShell::getName));
		this.products(searchString).forEach(product -> {
			if (!product.isDeleted()) {
				productShells.add(product.toShell(baseURL));
			}
		});

		return productShells;
	}

	public Product delete(final Product product, final Sudoers sudoers) {
		Preconditions.checkNotNull(product, "product can not be null");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null");
		product.delete(sudoers);
		return this.save(product);
	}

	public ProductCategory delete(final ProductCategory category, final Sudoers sudoers) {
		Preconditions.checkNotNull(category, "category can not be null");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null");

		final Set<Product> products = iWebAdminDbService.products(category, false);
		if (!products.isEmpty()) {
			throw new ServicesNotAcceptable(MessageUtils.CATEGORT_CONTAINS);
		}

		category.delete(sudoers);
		return this.save(category);

	}

}
