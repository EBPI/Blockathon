package ebpi.hackathon.hypertrace.web.domein;

import org.junit.Test;

public class ProductTest {
    @Test
    public void testProduct(){
        Product product = new Product();
        product.setCreator("Herr creator");
        product.setDescription("Bozie");
        product.setEnforcementDBUrl("www.enforce.de");
        product.setProductID("dirdgf");
        product.setValue("waarde is null");
        assert (product.getCreator().equals("Herr creator"));
        assert(product.getDescription().equals("Bozie"));
        assert(product.getEnforcementDBUrl().equals("www.enforce.de"));
        assert(product.getProductID().equals("dirdgf"));
        assert(product.getValue().equals("waarde is null"));

    }
}
