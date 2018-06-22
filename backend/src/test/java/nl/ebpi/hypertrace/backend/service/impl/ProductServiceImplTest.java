package nl.ebpi.hypertrace.backend.service.impl;

import nl.ebpi.hypertrace.backend.service.ProductService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class ProductServiceImplTest {
	@Autowired
	private ProductService productService;
	@Autowired
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;

	@Before
	public void setUp() throws Exception {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testRetrieveAllProducts() throws Exception {

		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.PRODUCTS.getUrl())).andRespond(MockRestResponseCreators.withSuccess(
				"[{\"$class\":\"org.ebpi.blockathon.Product\",\"ProductID\":\"NIKESHOE001\",\"EnforcementDBUrl\":\"http://www.nike.com\",\"Creator\":\"resource:org.ebpi.blockathon.Manufacturer#NIKE001\"}]",
				MediaType.APPLICATION_JSON));
		productService.retrieveAllProducts();
	}

	@Configuration
	@ComponentScan("nl.ebpi.hypertrace.backend.*")
	public static class SpringConfig {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}
}
