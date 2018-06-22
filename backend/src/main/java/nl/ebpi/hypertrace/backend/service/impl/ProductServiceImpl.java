package nl.ebpi.hypertrace.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainProduct;
import nl.ebpi.hypertrace.backend.generated.domain.Product;
import nl.ebpi.hypertrace.backend.service.ProductService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;

	public ProductServiceImpl() {
	}

	@Override
	public List<Product> retrieveAllProducts() {
		LOG.debug("Retrieving all products from the blockchain");

		List<BlockChainProduct> blockChainProducts = restTemplate.exchange(BlockchainRestUrl.PRODUCTS.getUrl(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<BlockChainProduct>>() {
				}).getBody();
		return blockChainProducts.stream().map(blp -> convertProduct(blp)).collect(Collectors.toList());
	}

	private Product convertProduct(BlockChainProduct blp) {
		Product p = new Product();
		p.setCreator(removeClass(blp.getCreator()));
		p.setProductID(blp.getProductID());
		return p;
	}

	private String removeClass(String input) {
		int i = input.indexOf("#");
		if (i > 0 && i < input.length() - 1) {
			return input.substring(i + 1);
		}
		return input;
	}

}
