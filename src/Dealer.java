public class Dealer {
    private String dealerId;
    private String dealerName;
    private String phoneNumber;
    private String location;

    public Dealer(String dealerId, String dealerName,
                  String phoneNumber, String location) {
        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public String getDealerId() {
        return dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        return dealerId + " | " + dealerName + " | " +
                phoneNumber + " | " + location;
    }
}