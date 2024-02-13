package ShoeWebshop.T;

public class Customer {
    private int id;
    private String fullName;
    private String address;
    private String locality;


    public Customer(int id, String fullName, String address, String locality) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.locality = locality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}