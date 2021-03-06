package com.chillies.smartshopper.service.dto;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chillies.smartshopper.common.shell.web.CartShell;
import com.chillies.smartshopper.common.shell.web.CategoryProductShell;
import com.chillies.smartshopper.common.shell.web.OrderShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.AppUtils;
import com.chillies.smartshopper.common.util.DateUtils;
import com.chillies.smartshopper.common.util.MessageUtils;
import com.chillies.smartshopper.common.util.OrderStatus;
import com.chillies.smartshopper.common.util.SuperCategory;
import com.chillies.smartshopper.lib.exception.DbException;
import com.chillies.smartshopper.lib.exception.NotAccatable;
import com.chillies.smartshopper.lib.exception.ServicesNotAcceptable;
import com.chillies.smartshopper.lib.exception.ServicesNotFound;
import com.chillies.smartshopper.lib.model.CreatedMeta;
import com.chillies.smartshopper.lib.model.DateMeta;
import com.chillies.smartshopper.lib.model.web.Cart;
import com.chillies.smartshopper.lib.model.web.Order;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.lib.model.web_model.Product;
import com.chillies.smartshopper.lib.model.web_model.ProductCategory;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.lib.util.ServiceUtils;
import com.chillies.smartshopper.service.model.ProductBody;
import com.chillies.smartshopper.service.model.ProductCategoryBody;
import com.chillies.smartshopper.service.model.ProductMetaBody;
import com.chillies.smartshopper.service.transaction.ProductTransaction;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;

/**
 * ProductDTO : is for converts body-object to entity-object, body-object
 * validation and entity-object to shell-objects.
 * 
 * As well as all the business logic will be written in this class.
 * 
 * @author Jinen Kothari
 *
 */
@Service
public class ProductDTO {

	private static final Logger LOG = LoggerFactory.getLogger(ProductDTO.class);

	@Autowired
	private ProductTransaction productTransaction;

	private double isValid(final String value) {
		Preconditions.checkNotNull(value, "value can not be null.");
		try {
			return AppUtils.stringToDouble(value);
		} catch (Exception e) {
			final String error = String.format("Given value is not double.");
			LOG.info("{} value is not double.", value);
			throw new NotAccatable(error);
		}
	}

	public ResponseEntity<ProductCategoryShell> addCategory(final ProductCategoryBody productCategoryBody,
			final Sudoers sudoers) {
		Preconditions.checkNotNull(productCategoryBody, "productCategoryBody can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		try {

			LOG.info(String.format("add() request %s", productCategoryBody.toString()));

			final String categoryName = productCategoryBody.getName();
			productTransaction.isProductCategoryName(categoryName);

			final ProductCategory productCategory = productTransaction.save(productCategoryBody.getSuperCategory(),
					categoryName, Optional.fromNullable(productCategoryBody.getRemark()),
					new DateMeta(Optional.absent()), new CreatedMeta(sudoers, Optional.absent()));

			final ResponseEntity<ProductCategoryShell> responseEntity = new ResponseEntity<ProductCategoryShell>(
					productCategory.toShell(), HttpStatus.OK);

			LOG.info(String.format("add() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("add() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("add() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductCategoryShell> updateCategory(ProductCategoryBody productCategoryBody,
			Sudoers sudoers) {
		Preconditions.checkNotNull(productCategoryBody, "productCategoryBody can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		try {

			LOG.info(String.format("update() request %s", productCategoryBody.toString()));

			final String categoryId = productCategoryBody.getId();

			if (categoryId == null) {
				throw new NotAccatable(MessageUtils.REQUIRED_FIELD);
			}

			final String categoryName = productCategoryBody.getName();
			final ProductCategory oldCategory = productTransaction.getProductCategoryId(categoryId);

			if (!categoryName.equals(oldCategory.getName())) {
				productTransaction.isProductCategoryName(categoryName);
			}

			final ProductCategory category = productTransaction.update(productCategoryBody.getSuperCategory(),
					oldCategory, categoryName, Optional.fromNullable(productCategoryBody.getRemark()), sudoers);

			final ResponseEntity<ProductCategoryShell> responseEntity = new ResponseEntity<ProductCategoryShell>(
					category.toShell(), HttpStatus.OK);

			LOG.info(String.format("update() response %s", responseEntity.toString()));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("update() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("update() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<ProductCategoryShell>> categories() {
		try {

			final SortedSet<ProductCategoryShell> productCategoryShells = productTransaction.sortedCategories();

			final ResponseEntity<SortedSet<ProductCategoryShell>> responseEntity = new ResponseEntity<SortedSet<ProductCategoryShell>>(
					productCategoryShells, HttpStatus.OK);

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("categories() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("categories() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductCategoryShell> getCategory(final String categoryId) {
		Preconditions.checkNotNull(categoryId, "categoryId can not be null.");
		try {

			LOG.info(String.format("getCategory() request %s", categoryId));

			final ProductCategory category = productTransaction.getProductCategoryId(categoryId);

			final ResponseEntity<ProductCategoryShell> responseEntity = new ResponseEntity<ProductCategoryShell>(
					category.toShell(), HttpStatus.OK);

			LOG.info(String.format("getCategory() response %s", responseEntity));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("getCategory() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("getCategory() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductShell> addProduct(final String productBody, final Optional<MultipartFile> productImage,
			final Sudoers sudoers, final HttpServletRequest request) {
		Preconditions.checkNotNull(productBody, "productBody can not be null.");
		Preconditions.checkNotNull(productImage, "productImage can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");
		try {

			LOG.info(String.format("addProduct() request %s", productBody));

			final ProductBody productBody2 = new Gson().fromJson(URLDecoder.decode(productBody, "UTF-8"),
					ProductBody.class);

			if (productBody2.getCategoryId() == null || productBody2.getName() == null) {
				throw new NotAccatable(MessageUtils.REQUIRED_FIELD);
			}

			final double price = this.isValid(productBody2.getPrice());
			final double points = this.isValid(productBody2.getPoints());

			productTransaction.isProductName(productBody2.getName());
			final ProductCategory productCategory = productTransaction
					.getProductCategoryId(productBody2.getCategoryId());

			final Optional<String> path;

			if (productImage.isPresent()) {
				path = Optional.fromNullable(AppUtils.addOn(productBody2.getName(), DateUtils.dateTimeToAddon()));
				productTransaction.uploadImageToDisk(productImage.get(), path.get());
			} else {
				path = Optional.absent();
			}

			final Product product = productTransaction.save(productBody2.getName(),
					Optional.fromNullable(productBody2.getRemark()), price, points, productCategory, sudoers, path);

			final ResponseEntity<ProductShell> responseEntity = new ResponseEntity<>(
					product.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("addProduct() response %s", responseEntity));
			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("addProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("addProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductShell> updateProduct(final String productBody,
			final Optional<MultipartFile> productImage, final Sudoers sudoers, final HttpServletRequest request) {
		Preconditions.checkNotNull(productBody, "productBody can not be null.");
		Preconditions.checkNotNull(productImage, "productImage can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");
		try {

			LOG.info(String.format("updateProduct() request %s", productBody));

			final ProductBody productBody2 = new Gson().fromJson(URLDecoder.decode(productBody, "UTF-8"),
					ProductBody.class);

			if (productBody2.getId() == null || productBody2.getCategoryId() == null
					|| productBody2.getName() == null) {
				throw new NotAccatable(MessageUtils.REQUIRED_FIELD);
			}

			final double price = this.isValid(productBody2.getPrice());
			final double points = this.isValid(productBody2.getPoints());

			final Product oldProduct = productTransaction.getProductById(productBody2.getId());
			if (!oldProduct.getName().equals(productBody2.getName())) {
				productTransaction.isProductName(productBody2.getName());
			}

			final ProductCategory productCategory = productTransaction
					.getProductCategoryId(productBody2.getCategoryId());

			final String path = oldProduct.getPath();

			if (productImage.isPresent()) {
				productTransaction.uploadImageToDisk(productImage.get(), path);
			}

			final Product product = productTransaction.update(oldProduct, productBody2.getName(),
					Optional.fromNullable(productBody2.getRemark()), price, points, productCategory, sudoers);

			final ResponseEntity<ProductShell> responseEntity = new ResponseEntity<>(
					product.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("updateProduct() response %s", responseEntity));
			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("updateProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("updateProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<ProductShell>> products(final HttpServletRequest request) {
		Preconditions.checkNotNull(request, "request can not be null.");
		try {

			final SortedSet<ProductShell> shells = productTransaction.sortedProducts(ServiceUtils.siteBaseUrl(request));

			final ResponseEntity<SortedSet<ProductShell>> responseEntity = new ResponseEntity<SortedSet<ProductShell>>(
					shells, HttpStatus.OK);

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("products() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("products() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductShell> getProductId(final String productId, final HttpServletRequest request) {
		Preconditions.checkNotNull(productId, "productId can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");
		try {

			LOG.info(String.format("getProductId() request %s", productId));

			final Product product = productTransaction.getProductById(productId);

			final ResponseEntity<ProductShell> responseEntity = new ResponseEntity<ProductShell>(
					product.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("getProductId() response %s", responseEntity));

			return responseEntity;

		} catch (DbException e) {
			final String error = String.format("getProductId() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("getProductId() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public InputStream getProductImage(final String productCode) {
		Preconditions.checkNotNull(productCode, "productCode can not be null.");

		LOG.info(String.format("productURL() request %s", productCode));

		try {
			final InputStream inputStream = productTransaction.getProductImage(productCode);

			return inputStream;
		} catch (DbException e) {
			final String error = String.format("getProductImage() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("getProductImage() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<ProductShell>> categorizedProduct(final String categoryId,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(categoryId, "categoryId can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("categorizedProduct() request %s", categoryId));

		try {

			final ProductCategory category = productTransaction.getProductCategoryId(categoryId);
			final SortedSet<ProductShell> shells = productTransaction
					.sortedCategorizedProduct(ServiceUtils.siteBaseUrl(request), category);

			final ResponseEntity<SortedSet<ProductShell>> responseEntity = new ResponseEntity<SortedSet<ProductShell>>(
					shells, HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("categorizedProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("categorizedProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<Set<CategoryProductShell>> categorizedProducts(final HttpServletRequest request) {
		Preconditions.checkNotNull(request, "request can not be null.");

		try {
			final SortedSet<CategoryProductShell> categoryShells = new TreeSet<>(
					Comparator.comparing(CategoryProductShell::getName));

			final Stream<ProductCategory> stream = productTransaction.categories().stream().limit(10);
			stream.forEach(category -> {
				final SortedSet<ProductShell> shells = productTransaction
						.sortedCategorizedProduct(ServiceUtils.siteBaseUrl(request), category, true);

				if (!category.isDeleted()) {
					categoryShells.add(new CategoryProductShell(category.toShell(), shells));
				}
			});

			final ResponseEntity<Set<CategoryProductShell>> responseEntity = new ResponseEntity<Set<CategoryProductShell>>(
					categoryShells, HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("categorizedProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("categorizedProduct() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<CartShell> addToCart(final Users users, final ProductMetaBody productMetaBody,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(productMetaBody, "productMetaBody can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("addToCart() request %s", productMetaBody.toString()));

		try {

			final Product product = productTransaction.getProductById(productMetaBody.getProductId());
			final Cart cart = productTransaction.addOrUpdateCart(users, product, productMetaBody.getQuantity());

			final ResponseEntity<CartShell> responseEntity = new ResponseEntity<CartShell>(
					cart.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("addToCart() response %s", responseEntity.toString()));

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("addToCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("addToCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<CartShell> removeFromCart(final Users users, final String productId,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(productId, "productId can not be null.");

		LOG.info(String.format("removeFromCart() from users %s, product %s", users.toString(), productId));

		try {

			final Product product = productTransaction.getProductById(productId);

			final Optional<Cart> optionalCart = productTransaction.getCartByUser(users);
			if (!optionalCart.isPresent()) {
				throw new NotAccatable(MessageUtils.CART_AVAIABLE);
			}

			final Cart cart = productTransaction.removeProductFromCart(optionalCart.get(), product);

			final ResponseEntity<CartShell> responseEntity = new ResponseEntity<CartShell>(
					cart.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("removeFromCart() response %s", responseEntity.toString()));

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("removeFromCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("removeFromCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<CartShell> getCart(final Users users, final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		try {
			final Optional<Cart> optional = productTransaction.getCartByUser(users);

			if (!optional.isPresent()) {
				return new ResponseEntity<CartShell>(HttpStatus.OK);

			}

			final Cart cart = optional.get();
			final ResponseEntity<CartShell> responseEntity = new ResponseEntity<CartShell>(
					cart.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("getCart() response %s", responseEntity.toString()));

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("getCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("getCart() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<OrderShell> orderPlace(final Users users, final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("orderPlace() from users %s", users.toString()));

		try {
			final Optional<Cart> optional = productTransaction.getCartByUser(users);

			if (!optional.isPresent()) {
				throw new NotAccatable(MessageUtils.CART_AVAIABLE);
			}
			final Cart cart = optional.get();

			final Order order = productTransaction.placeOrder(cart, users);
			productTransaction.checkStatus(cart);
			final ResponseEntity<OrderShell> responseEntity = new ResponseEntity<OrderShell>(
					order.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			LOG.info(String.format("orderPlace() response %s", responseEntity.toString()));

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("orderPlace() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("orderPlace() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<OrderShell>> orders(final Users users, final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("orders() from users %s", users.toString()));

		try {

			final SortedSet<OrderShell> orderShells = productTransaction.sortedOrders(users,
					ServiceUtils.siteBaseUrl(request));

			final ResponseEntity<SortedSet<OrderShell>> responseEntity = new ResponseEntity<SortedSet<OrderShell>>(
					orderShells, HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("orders() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("orders() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<OrderShell>> orders(final HttpServletRequest request) {
		Preconditions.checkNotNull(request, "request can not be null.");

		try {

			final SortedSet<OrderShell> orderShells = productTransaction
					.sortedOrders(ServiceUtils.siteBaseUrl(request));

			final ResponseEntity<SortedSet<OrderShell>> responseEntity = new ResponseEntity<>(orderShells,
					HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("orders() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("orders() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<OrderShell> orderCancel(final Users users, final String orderId,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(users, "users can not be null.");
		Preconditions.checkNotNull(orderId, "orderId can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("orderCancel() from users %s, orderId %s", users.toString(), orderId));

		try {
			final Order order = productTransaction.cancelOrder(productTransaction.getOrderById(orderId));

			final ResponseEntity<OrderShell> responseEntity = new ResponseEntity<OrderShell>(
					order.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("orderCancel() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("orderCancel() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<OrderShell> status(final OrderStatus status, final String orderId, final Sudoers sudoers,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(status, "status can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(orderId, "orderId can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info(String.format("status() from admin %s, orderId %s to status %s", sudoers.toString(), orderId, status));

		try {
			final Order order = productTransaction.orderStatusUpdate(productTransaction.getOrderById(orderId), status,
					sudoers);

			final ResponseEntity<OrderShell> responseEntity = new ResponseEntity<OrderShell>(
					order.toShell(ServiceUtils.siteBaseUrl(request)), HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("status() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("status() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<Set<SuperCategory>> superCategories() {
		try {

			final SuperCategory[] categories = SuperCategory.values();
			final ResponseEntity<Set<SuperCategory>> responseEntity = new ResponseEntity<>(
					new HashSet<>(Arrays.asList(categories)), HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("status() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("status() Failed with message : %s", e.getMessage());
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<SortedSet<ProductShell>> search(final String searchString, final HttpServletRequest request) {
		Preconditions.checkNotNull(searchString, "searchString can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info("search() request {}", searchString);

		try {
			final SortedSet<ProductShell> productShells = productTransaction.search(searchString,
					ServiceUtils.siteBaseUrl(request));
			final ResponseEntity<SortedSet<ProductShell>> responseEntity = new ResponseEntity<>(productShells,
					HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("search() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("search() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductShell> deleteProduct(final String productId, final Sudoers sudoers,
			final HttpServletRequest request) {
		Preconditions.checkNotNull(productId, "productId can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");
		Preconditions.checkNotNull(request, "request can not be null.");

		LOG.info("deleteProduct() request {}", productId);

		try {
			final Product product = productTransaction.getProductById(productId);
			final ProductShell productShell = productTransaction.delete(product, sudoers)
					.toShell(ServiceUtils.siteBaseUrl(request));
			final ResponseEntity<ProductShell> responseEntity = new ResponseEntity<>(productShell, HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("deleteProduct() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("deleteProduct() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

	public ResponseEntity<ProductCategoryShell> deleteCategory(final String categoryId, final Sudoers sudoers) {
		Preconditions.checkNotNull(categoryId, "categoryId can not be null.");
		Preconditions.checkNotNull(sudoers, "sudoers can not be null.");

		LOG.info("deleteCategory() request {}", categoryId);

		try {
			final ProductCategory category = productTransaction.getProductCategoryId(categoryId);
			final ProductCategoryShell productCategoryShell = productTransaction.delete(category, sudoers).toShell();

			final ResponseEntity<ProductCategoryShell> responseEntity = new ResponseEntity<>(productCategoryShell,
					HttpStatus.OK);

			return responseEntity;
		} catch (DbException e) {
			final String error = String.format("deleteCategory() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotAcceptable(error);
		} catch (ServicesNotAcceptable | ServicesNotFound | NotAccatable e) {
			final String error = String.format(e.getMessage());
			LOG.info(error);
			throw new ServicesNotAcceptable(error);
		} catch (Exception e) {
			final String error = String.format("deleteCategory() Failed with message : %s", e);
			LOG.error(error);
			throw new ServicesNotFound(error);
		}
	}

}
