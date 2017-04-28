package com.chillies.smartshopper.web.service;

import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chillies.smartshopper.common.shell.web.CategoryProductShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductCategoryShell;
import com.chillies.smartshopper.common.shell.web_admin.ProductShell;
import com.chillies.smartshopper.service.dto.ProductDTO;

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

}
