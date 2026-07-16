public class CartItem {
    private InventoryItem item;
    private int quantity;

    public CartItem(InventoryItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public InventoryItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotalBeforeDiscount() {
        return item.getPrice() * quantity;
    }

    public double getSubtotalAfterDiscount() {
        double subtotal = getSubtotalBeforeDiscount();

        if (quantity >= 3) {
            subtotal = subtotal * 0.95;
        }

        return subtotal;
    }

    public String toString() {
        return item.getPartCode() + " | " +
                item.getPartName() + " | Qty: " +
                quantity + " | Subtotal: Rs. " +
                getSubtotalAfterDiscount();
    }
}