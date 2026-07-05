public class InventoryItem {
    private String partCode;
    private String partName;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String dateAdded;
    private String imageFile;


    public InventoryItem(String partCode, String partName, String brand,
                         double price, int quantity, String category,
                         String dateAdded, String imageFile) {
        this.partCode = partCode;
        this.partName = partName;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imageFile = imageFile;
    }

    public String getPartCode() {
        return partCode;
    }

    public String getPartName() {
        return partName;
    }

    public String getBrand() {
        return brand;
    }

    public String getPrice() {
        return price;
    }
    public String getquantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
    public double getTotalVlue(){
        return price*quantity;
    }
    public String toString(){
        return partCode+"|"+partName+"|"+brand+"| Rs."+
                price+"|Qty:"+quantity+"|"+category;
    }
}


