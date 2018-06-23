package ebpi.hackathon.hypertrace.web.domein;

public class Product {
    private String ProductID;
    private String Description;
    private String Value;
    private String EnforcementDBUrl;
    private String Creator;

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getEnforcementDBUrl() {
        return EnforcementDBUrl;
    }

    public void setEnforcementDBUrl(String enforcementDBUrl) {
        EnforcementDBUrl = enforcementDBUrl;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }
}
