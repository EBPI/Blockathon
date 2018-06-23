package ebpi.hackathon.hypertrace.web.domein;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    @Test
    public void testGettersAndSetters(){
        Order order = new Order();
        order.setManufacturer("blee");
        order.setOrderer("eurderer");
        List<String> ding = new ArrayList<>();
        ding.add("hoi");
        ding.add("tot Ziens");
        order.setProducts(ding);
        assert(order.getProducts().get(0).equals("hoi"));
        assert(order.getProducts().get(1).equals("tot Ziens"));
        assert(order.getManufacturer().equals("blee"));
        assert(order.getOrderer().equals(("eurderer")));




    }

}
