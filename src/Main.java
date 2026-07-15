import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ArrayList<InventoryItem> items =
                FileHandler.loadInventory("inventory_legacy.txt");

        ArrayList<Dealer> dealers =
                FileHandler.loadDealers("dealers_legacy.txt");

        InventoryManager inventoryManager = new InventoryManager(items);
        DealerManager dealerManager = new DealerManager(dealers);
        CartManager cartManager = new CartManager(inventoryManager);

        Scanner input = new Scanner(System.in);

        int choice = 0;

        while (choice != 11) {
            System.out.println("\n========== MALABE SPARES DEPOT ==========");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Part");
            System.out.println("3. Update Part");
            System.out.println("4. Delete Part");
            System.out.println("5. Search Parts");
            System.out.println("6. View Low Stock Items");
            System.out.println("7. View All Dealers");
            System.out.println("8. Select Random Four Dealers");
            System.out.println("9. Add Item to Cart");
            System.out.println("10. Checkout Cart");
            System.out.println("11. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (choice == 1) {
                inventoryManager.viewAllItems();

            } else if (choice == 2) {
                System.out.print("Enter part code: ");
                String code = input.nextLine();

                System.out.print("Enter part name: ");
                String name = input.nextLine();

                System.out.print("Enter brand: ");
                String brand = input.nextLine();

                double price;

                while (true) {
                    try {
                        System.out.print("Enter price: ");
                        price = Double.parseDouble(input.nextLine());

                        if (price <= 0) {
                            System.out.println("Invalid price! Price must be greater than 0.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price! Please enter numbers only.");
                    }
                }
                System.out.println("Price = " + price);
                int quantity;
                while (true) {
                    try {
                        System.out.print("Enter quantity: ");
                        quantity = Integer.parseInt(input.nextLine());

                        if (quantity < 0) {
                            System.out.println("Invalid quantity! Quantity cannot be negative.");
                        } else {
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity! Please enter a whole number.");
                    }
                }

                System.out.println("Quantity = " + quantity);
                System.out.print("Enter category: ");
                String category = input.nextLine();

                System.out.print("Enter date added: ");
                String date = input.nextLine();

                System.out.print("Enter image file: ");
                String image = input.nextLine();

                InventoryItem item = new InventoryItem(
                        code, name, brand, price, quantity, category, date, image
                );

                inventoryManager.addItem(item);

            } else if (choice == 3) {
                System.out.print("Enter part code to update: ");
                String code = input.nextLine();

                System.out.print("Enter new price: ");
                double price = Double.parseDouble(input.nextLine());

                System.out.print("Enter new quantity: ");
                int quantity = Integer.parseInt(input.nextLine());

                inventoryManager.updateItem(code, price, quantity);

            } else if (choice == 4) {
                System.out.print("Enter part code to delete: ");
                String code = input.nextLine();

                inventoryManager.deleteItem(code);

            } else if (choice == 5) {
                System.out.print("Enter category or all: ");
                String category = input.nextLine();

                System.out.print("Enter minimum price: ");
                double minPrice = Double.parseDouble(input.nextLine());

                System.out.print("Enter maximum price: ");
                double maxPrice = Double.parseDouble(input.nextLine());

                System.out.print("Enter keyword: ");
                String keyword = input.nextLine();

                inventoryManager.searchItems(category, minPrice, maxPrice, keyword);

            } else if (choice == 6) {
                inventoryManager.showLowStockItems();

            } else if (choice == 7) {
                dealerManager.viewAllDealers();

            } else if (choice == 8) {
                dealerManager.selectRandomFourDealers();

            } else if (choice == 9) {
                System.out.print("Enter part code: ");
                String code = input.nextLine();

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(input.nextLine());

                cartManager.addToCart(code, quantity);
                cartManager.viewCart();

            } else if (choice == 10) {
                cartManager.checkout();

            } else if (choice == 11) {
                System.out.println("Exiting program...");

            } else {
                System.out.println("Invalid choice.");
            }
        }

        input.close();
    }
}