package com.chillies.smartshopper.web.service;

import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.web.CartShell;
import com.chillies.smartshopper.common.shell.web.CategoryProductShell;
import com.chillies.smartshopper.common.shell.web.OrderShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.lib.model.web.Users;
import com.chillies.smartshopper.service.dto.ProductDTO;
import com.chillies.smartshopper.service.dto.UsersDTO;
import com.chillies.smartshopper.service.model.ProductMetaBody;

/**
 * ProductService : is @RestController for user login, logout & session
 * management.
 * 
 * @author Jinen Kothari
 *
 */
@RestController
@RequestMapping(value = "${service}${product}")
public class ProductService {

	@Autowired
	private ProductDTO productDTO;

	@Autowired
	private UsersDTO usersDTO;

	@RequestMapping(value = "${service.product.categories}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<ProductCategoryShell>> categories() {
		return productDTO.categories();
	}

	@RequestMapping(value = "${service.product.category.product}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<ProductShell>> categorizedProduct(
			@RequestParam(value = "categoryId", required = true) String categoryId, final HttpServletRequest request) {
		return productDTO.categorizedProduct(categoryId, request);
	}

	@RequestMapping(value = "/productURL/{productCode}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public void getProductImage(@PathVariable String productCode, HttpServletResponse response) throws IOException {
		IOUtils.copy(productDTO.getProductImage(productCode), response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(value = "${service.product.category.products}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<CategoryProductShell>> categorizedProducts(final HttpServletRequest request) {
		return productDTO.categorizedProducts(request);
	}

	// auth services
	@RequestMapping(value = "${service.users.cart.add}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartShell> addToCart(@RequestParam(value = "session", required = true) String session,
			@RequestBody @Valid ProductMetaBody productMetaBody, final HttpServletRequest request) {

		final Users users = usersDTO.isValid(session);
		return productDTO.addToCart(users, productMetaBody, request);
	}

	@RequestMapping(value = "${service.users.cart.get}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartShell> getCart(@RequestParam(value = "session", required = true) String session,
			final HttpServletRequest request) {

		final Users users = usersDTO.isValid(session);
		return productDTO.getCart(users, request);
	}

	@RequestMapping(value = "${service.users.cart.remove}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartShell> removeFromCart(@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "productId", required = true) String productId, final HttpServletRequest request) {

		final Users users = usersDTO.isValid(session);
		return productDTO.removeFromCart(users, productId, request);
	}

	@RequestMapping(value = "${service.users.order.place}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderShell> orderPlace(@RequestParam(value = "session", required = true) String session,
			final HttpServletRequest request) {
		final Users users = usersDTO.isValid(session);
		return productDTO.orderPlace(users, request);
	}

	@RequestMapping(value = "${service.users.order.orders}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<OrderShell>> orders(
			@RequestParam(value = "session", required = true) String session, final HttpServletRequest request) {
		final Users users = usersDTO.isValid(session);
		return productDTO.orders(users, request);
	}

	@RequestMapping(value = "${service.users.order.cancel}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderShell> orderCancel(
			@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "orderId", required = true) String orderId, final HttpServletRequest request) {
		final Users users = usersDTO.isValid(session);
		return productDTO.orderCancel(users, orderId, request);
	}

}
