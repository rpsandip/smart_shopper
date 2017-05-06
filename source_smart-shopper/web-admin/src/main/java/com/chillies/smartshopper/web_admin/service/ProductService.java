package com.chillies.smartshopper.web_admin.service;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.chillies.smartshopper.common.shell.web.OrderShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.common.util.OrderStatus;
import com.chillies.smartshopper.lib.model.web_model.Sudoers;
import com.chillies.smartshopper.service.dto.ProductDTO;
import com.chillies.smartshopper.service.dto.SudoersDTO;
import com.chillies.smartshopper.service.model.ProductCategoryBody;
import com.google.common.base.Optional;

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
	private SudoersDTO sudoersDTO;

	@RequestMapping(value = "${service.sudoers.product.category.add}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategoryShell> addCategory(@Valid @RequestBody ProductCategoryBody productCategoryBody,
			@RequestParam(value = "session", required = true) String session) {
		final Sudoers sudoers = sudoersDTO.isValid(session);
		return productDTO.addCategory(productCategoryBody, sudoers);

	}

	@RequestMapping(value = "${service.sudoers.product.category.categories}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<ProductCategoryShell>> categories(
			@RequestParam(value = "session", required = true) String session) {
		sudoersDTO.isValid(session);
		return productDTO.categories();
	}

	@RequestMapping(value = "${service.sudoers.product.category.get}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductCategoryShell> getCategory(
			@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "categoryId", required = true) String categoryId) {
		sudoersDTO.isValid(session);
		return productDTO.getCategory(categoryId);
	}

	@RequestMapping(value = "${service.sudoers.product.add}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductShell> addProduct(
			@RequestParam(value = "productBody", required = true) String productBody,
			@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "productImage", required = false) MultipartFile productImage,
			HttpServletRequest request) {

		final Sudoers sudoers = sudoersDTO.isValid(session);
		return productDTO.addProduct(productBody, Optional.fromNullable(productImage), sudoers, request);

	}

	@RequestMapping(value = "${service.sudoers.product.products}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<ProductShell>> products(
			@RequestParam(value = "session", required = true) String session, HttpServletRequest request) {
		sudoersDTO.isValid(session);
		return productDTO.products(request);
	}

	@RequestMapping(value = "${service.sudoers.product.get}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductShell> getProduct(@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "productId", required = true) String productId, HttpServletRequest request) {
		sudoersDTO.isValid(session);
		return productDTO.getProductId(productId, request);
	}

	@RequestMapping(value = "/productURL/{productCode}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public void getProductImage(@PathVariable String productCode, HttpServletResponse response) throws IOException {
		IOUtils.copy(productDTO.getProductImage(productCode), response.getOutputStream());
		response.flushBuffer();
	}

	@RequestMapping(value = "${service.sudoers.order.orders}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SortedSet<OrderShell>> orders(
			@RequestParam(value = "session", required = true) String session, final HttpServletRequest request) {
		sudoersDTO.isValid(session);
		return productDTO.orders(request);
	}

	@RequestMapping(value = "${service.sudoers.order.status}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderShell> status(
			@RequestParam(value = "session", required = true) String session,
			@RequestParam(value = "status", required = true) OrderStatus status,
			@RequestParam(value = "orderId", required = true) String orderId, final HttpServletRequest request) {
		final Sudoers sudoers = sudoersDTO.isValid(session);
		return productDTO.status(status, orderId, sudoers, request);
	}

}
