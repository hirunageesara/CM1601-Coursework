import java.util.ArrayList;

public class CartManager {
    private ArrayList<CartItem> cart;
    private InventoryManager inventoryManager;

    public CartManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.cart = new ArrayList<>();
    }

    public void addToCart(String code, int quantity) {
        InventoryItem item = inventoryManager.findItemByCode(code);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (quantity > item.getQuantity()) {
            System.out.println("Not enough stock available.");
            return;
        }

        cart.add(new CartItem(item, quantity));
        System.out.println("Item added to cart.");
    }

    public void viewCart() {
        System.out.println("\n========== CART ==========");

        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        for (CartItem cartItem : cart) {
            System.out.println(cartItem);
        }

        System.out.println("Cart Total: Rs. " + calculateTotal());
    }

    public double calculateTotal() {
        double total = 0;

        for (CartItem cartItem : cart) {
            total += cartItem.getSubtotalAfterDiscount();
        }

        if (hasEnginePart() && hasElectricalPart()) {
            total = total * 0.90;
        }

        return total;
    }

    private boolean hasEnginePart() {
        for (CartItem cartItem : cart) {
            if (cartItem.getItem().getCategory().equalsIgnoreCase("Engine")) {
                return true;
            }
        }

        return false;
    }

    private boolean hasElectricalPart() {
        for (CartItem cartItem : cart) {
            if (cartItem.getItem().getCategory().equalsIgnoreCase("Electrical")) {
                return true;
            }
        }

        return false;
    }

    public void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cannot checkout. Cart is empty.");
            return;
        }

        double total = calculateTotal();

        for (CartItem cartItem : cart) {
            InventoryItem item = cartItem.getItem();
            int newQuantity = item.getQuantity() - cartItem.getQuantity();
            item.setQuantity(newQuantity);

            AuditLogger.log("CHECKOUT", item.getPartCode(), cartItem.getQuantity());
        }

        FileHandler.saveInventory("inventory_legacy.txt", inventoryManager.getItems());

        System.out.println("Checkout completed successfully.");
        System.out.println("Final Total: Rs. " + total);

        cart.clear();
    }

    public void clearCart() {
        cart.clear();
        System.out.println("Cart cleared.");
    }
}