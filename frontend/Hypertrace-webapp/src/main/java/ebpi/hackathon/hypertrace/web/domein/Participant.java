package ebpi.hackathon.hypertrace.web.domein;

public class Participant {
    private String Name;
    private String PartnerID;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPartnerId() {
        return PartnerID;
    }

    public void setPartnerId(String parnterId) {
        this.PartnerID = parnterId;
    }
}
