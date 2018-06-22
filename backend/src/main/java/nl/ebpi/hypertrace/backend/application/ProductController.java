package nl.ebpi.hypertrace.backend.application;

import java.util.List;
import nl.ebpi.hypertrace.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping("/products")
	public List<nl.ebpi.hypertrace.backend.generated.domain.Product> allProducts() {
		return productService.retrieveAllProducts();
	}
}