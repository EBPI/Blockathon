package ebpi.hackathon.hypertrace.web.domein;

import java.util.List;

public class Order {
    private List<String> Products;
    private String Manufacturer;
    private String Orderer;

    public List<String> getProducts() {
        return Products;
    }

    public void setProducts(List<String> products) {
        Products = products;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getOrderer() {
        return Orderer;
    }

    public void setOrderer(String orderer) {
        Orderer = orderer;
    }
}
