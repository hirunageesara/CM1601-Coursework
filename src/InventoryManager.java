import java.util.ArrayList;

public class InventoryManager {
    private ArrayList<InventoryItem> items;
    private int lowStockThreshold;

    public InventoryManager(ArrayList<InventoryItem> items) {
        this.items = items;
        this.lowStockThreshold = 10;
    }

    public void viewAllItems() {
        sortByCategoryAndCode();

        System.out.println("\nINVENTORY ITEMS");

        for (InventoryItem item : items) {
            System.out.println(item);
        }

        System.out.println("------------------------------------");
        System.out.println("Total Parts: " + getTotalParts());
        System.out.println("Total Inventory Value: Rs. " + getTotalValue());
    }

    public void addItem(InventoryItem newItem) {
        if (findItemByCode(newItem.getPartCode()) != null) {
            System.out.println("Item code already exists.");
            return;
        }

        items.add(newItem);
        AuditLogger.log("ADD PART", newItem.getPartCode(), newItem.getQuantity());
        FileHandler.saveInventory("inventory_legacy.txt", items);

        System.out.println("Item added successfully.");
    }

    public void deleteItem(String code) {
        InventoryItem item = findItemByCode(code);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        items.remove(item);
        AuditLogger.log("DELETE PART", code, item.getQuantity());
        FileHandler.saveInventory("inventory_legacy.txt", items);

        System.out.println("Item deleted successfully.");
    }

    public void updateItem(String code, double newPrice, int newQuantity) {
        InventoryItem item = findItemByCode(code);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (newPrice < 0 || newQuantity < 0) {
            System.out.println("Invalid price or quantity.");
            return;
        }

        item.setPrice(newPrice);
        item.setQuantity(newQuantity);

        AuditLogger.log("UPDATE PART", code, newQuantity);
        FileHandler.saveInventory("inventory_legacy.txt", items);

        System.out.println("Item updated successfully.");
    }

    public InventoryItem findItemByCode(String code) {
        for (InventoryItem item : items) {
            if (item.getPartCode().equalsIgnoreCase(code)) {
                return item;
            }
        }

        return null;
    }

    public void searchItems(String category, double minPrice,
                            double maxPrice, String keyword) {
        System.out.println("\n========== SEARCH RESULTS ==========");

        boolean found = false;

        for (InventoryItem item : items) {
            boolean categoryMatch = category.equalsIgnoreCase("all") ||
                    item.getCategory().equalsIgnoreCase(category);

            boolean priceMatch = item.getPrice() >= minPrice &&
                    item.getPrice() <= maxPrice;

            boolean keywordMatch = item.getPartName().toLowerCase()
                    .contains(keyword.toLowerCase()) ||
                    item.getBrand().toLowerCase()
                            .contains(keyword.toLowerCase()) ||
                    item.getPartCode().toLowerCase()
                            .contains(keyword.toLowerCase());

            if (categoryMatch && priceMatch && keywordMatch) {
                System.out.println(item);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching items found.");
        }
    }

    public void showLowStockItems() {
        System.out.println("\n========== LOW STOCK ITEMS ==========");

        boolean found = false;

        for (InventoryItem item : items) {
            if (item.getQuantity() < lowStockThreshold) {
                System.out.println(item);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No low stock items found.");
        }
    }

    public void setLowStockThreshold(int threshold) {
        if (threshold <= 0) {
            System.out.println("Threshold must be greater than 0.");
            return;
        }

        this.lowStockThreshold = threshold;
        System.out.println("Low stock threshold updated to " + threshold);
    }

    public int getTotalParts() {
        return items.size();
    }

    public double getTotalValue() {
        double total = 0;

        for (InventoryItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void sortByCategoryAndCode() {
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = 0; j < items.size() - i - 1; j++) {
                InventoryItem item1 = items.get(j);
                InventoryItem item2 = items.get(j + 1);

                int categoryCompare = item1.getCategory()
                        .compareToIgnoreCase(item2.getCategory());

                boolean shouldSwap = false;

                if (categoryCompare > 0) {
                    shouldSwap = true;
                } else if (categoryCompare == 0 &&
                        item1.getPartCode().compareToIgnoreCase(item2.getPartCode()) > 0) {
                    shouldSwap = true;
                }

                if (shouldSwap) {
                    InventoryItem temp = items.get(j);
                    items.set(j, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
    }

    public ArrayList<InventoryItem> getItems() {
        return items;
    }
}
