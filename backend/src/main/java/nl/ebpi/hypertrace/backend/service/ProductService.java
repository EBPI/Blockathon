package nl.ebpi.hypertrace.backend.service;

import java.util.List;
import nl.ebpi.hypertrace.backend.generated.domain.Product;

public interface ProductService {

	/**
	 * Retrieve all products from the blockchain.
	 *
	 * @return List<Product>
	 */
	List<Product> retrieveAllProducts();

}