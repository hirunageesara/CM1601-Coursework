import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;


public class MainController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<InventoryItem> inventoryTable;

    @FXML
    private TableColumn<InventoryItem, String> colCode;

    @FXML
    private TableColumn<InventoryItem, String> colName;

    @FXML
    private TableColumn<InventoryItem, String> colBrand;

    @FXML
    private TableColumn<InventoryItem, Double> colPrice;

    @FXML
    private TableColumn<InventoryItem, Integer> colQuantity;

    @FXML
    private TableColumn<InventoryItem, String> colCategory;

    @FXML
    private TableColumn<InventoryItem, String> colDateAdded;

    private ArrayList<InventoryItem> inventoryItems;
    private InventoryManager inventoryManager;
    private CartManager cartManager;
    private InventoryItem lastSelectedItem;

    @FXML
    public void initialize() {

        titleLabel.setText(
                "Malabe Spares Depot Inventory System"
        );

        inventoryTable.getSelectionModel()
                .setSelectionMode(SelectionMode.SINGLE);

        colCode.setCellValueFactory(
                new PropertyValueFactory<>("partCode")
        );

        colName.setCellValueFactory(
                new PropertyValueFactory<>("partName")
        );

        colBrand.setCellValueFactory(
                new PropertyValueFactory<>("brand")
        );

        colPrice.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        colQuantity.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        colCategory.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        colDateAdded.setCellValueFactory(
                new PropertyValueFactory<>("dateAdded")
        );

        inventoryTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldItem, newItem) -> {

                    if (newItem != null) {

                        lastSelectedItem = newItem;

                        statusLabel.setText(
                                "Selected: "
                                        + newItem.getPartCode()
                                        + " - "
                                        + newItem.getPartName()
                        );
                    }
                });

        loadInventory();
        refreshInventoryTable();
    }
    private void loadInventory() {

        inventoryItems = FileHandler.loadInventory(
                "inventory_legacy.txt"
        );

        inventoryManager = new InventoryManager(
                inventoryItems
        );

        cartManager = new CartManager(
                inventoryManager
        );
    }

    private void refreshInventoryTable() {

        InventoryItem previouslySelected =
                inventoryTable.getSelectionModel()
                        .getSelectedItem();

        inventoryTable.setItems(
                FXCollections.observableArrayList(
                        inventoryManager.getItems()
                )
        );

        inventoryTable.refresh();

        if (previouslySelected != null) {

            for (InventoryItem item :
                    inventoryTable.getItems()) {

                if (item.getPartCode()
                        .equalsIgnoreCase(
                                previouslySelected.getPartCode()
                        )) {

                    inventoryTable.getSelectionModel()
                            .select(item);

                    inventoryTable.scrollTo(item);
                    break;
                }
            }
        }

        statusLabel.setText(
                inventoryManager.getTotalParts()
                        + " parts displayed | Total value: Rs. "
                        + String.format(
                        "%.2f",
                        inventoryManager.getTotalValue()
                )
        );
    }

    // =====================================================
    // VIEW INVENTORY
    // =====================================================

    @FXML
    private void handleViewInventory() {

        inventoryManager.sortByCategoryAndCode();
        refreshInventoryTable();

        statusLabel.setText(
                "Inventory sorted by category and part code."
        );
    }

    // =====================================================
    // ADD PART
    // =====================================================

    @FXML
    private void handleAddPart() {

        String code = getTextInput(
                "Add Part",
                "Enter Part Code",
                "Part Code:"
        );

        if (code == null) {
            return;
        }

        if (code.isEmpty()) {
            showError(
                    "Invalid Input",
                    "Part code cannot be empty."
            );
            return;
        }

        if (inventoryManager.findItemByCode(code) != null) {
            showError(
                    "Duplicate Code",
                    "This part code already exists."
            );
            return;
        }

        String name = getTextInput(
                "Add Part",
                "Enter Part Name",
                "Part Name:"
        );

        if (name == null || name.isEmpty()) {
            showError(
                    "Invalid Input",
                    "Part name cannot be empty."
            );
            return;
        }

        String brand = getTextInput(
                "Add Part",
                "Enter Brand",
                "Brand:"
        );

        if (brand == null || brand.isEmpty()) {
            showError(
                    "Invalid Input",
                    "Brand cannot be empty."
            );
            return;
        }

        Double price = getValidPrice(
                "Add Part",
                "Enter Price"
        );

        if (price == null) {
            return;
        }

        Integer quantity = getValidQuantity(
                "Add Part",
                "Enter Quantity"
        );

        if (quantity == null) {
            return;
        }

        String category = getTextInput(
                "Add Part",
                "Enter Category",
                "Engine, Electrical, Bodywork or Brakes:"
        );

        if (category == null || category.isEmpty()) {
            showError(
                    "Invalid Input",
                    "Category cannot be empty."
            );
            return;
        }

        InventoryItem newItem = new InventoryItem(
                code,
                name,
                brand,
                price,
                quantity,
                category,
                LocalDate.now().toString(),
                "no_image.png"
        );

        inventoryManager.addItem(newItem);

        refreshInventoryTable();

        showMessage(
                "Part Added",
                "Part added successfully."
        );
    }

    // =====================================================
    // UPDATE PART
    // =====================================================

    @FXML
    private void handleUpdatePart() {

        InventoryItem selectedItem =
                getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        Double newPrice = getValidPrice(
                "Update Part",
                "Enter new price for "
                        + selectedItem.getPartName()
        );

        if (newPrice == null) {
            return;
        }

        Integer newQuantity = getValidQuantity(
                "Update Part",
                "Enter new quantity for "
                        + selectedItem.getPartName()
        );

        if (newQuantity == null) {
            return;
        }

        inventoryManager.updateItem(
                selectedItem.getPartCode(),
                newPrice,
                newQuantity
        );

        refreshInventoryTable();

        showMessage(
                "Part Updated",
                "Part updated successfully."
        );
    }

    // =====================================================
    // DELETE PART
    // =====================================================

    @FXML
    private void handleDeletePart() {

        InventoryItem selectedItem =
                getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Delete Part");

        confirmation.setHeaderText(
                "Delete "
                        + selectedItem.getPartName()
                        + "?"
        );

        confirmation.setContentText(
                "Part Code: "
                        + selectedItem.getPartCode()
        );

        Optional<ButtonType> answer =
                confirmation.showAndWait();

        if (answer.isPresent()
                && answer.get() == ButtonType.OK) {

            inventoryManager.deleteItem(
                    selectedItem.getPartCode()
            );

            refreshInventoryTable();

            showMessage(
                    "Part Deleted",
                    "Part deleted successfully."
            );
        }
    }

    // =====================================================
    // SEARCH PARTS
    // =====================================================

    @FXML
    private void handleSearchParts() {

        String keyword = getTextInput(
                "Search Parts",
                "Search by code, name or brand",
                "Keyword:"
        );

        if (keyword == null) {
            return;
        }

        keyword = keyword.toLowerCase();

        ArrayList<InventoryItem> results =
                new ArrayList<>();

        for (InventoryItem item :
                inventoryManager.getItems()) {

            boolean matches =
                    item.getPartCode()
                            .toLowerCase()
                            .contains(keyword)
                            ||
                            item.getPartName()
                                    .toLowerCase()
                                    .contains(keyword)
                            ||
                            item.getBrand()
                                    .toLowerCase()
                                    .contains(keyword)
                            ||
                            item.getCategory()
                                    .toLowerCase()
                                    .contains(keyword);

            if (matches) {
                results.add(item);
            }
        }

        inventoryTable.setItems(
                FXCollections.observableArrayList(
                        results
                )
        );

        inventoryTable.refresh();

        statusLabel.setText(
                results.size()
                        + " matching item(s) found."
        );

        if (results.isEmpty()) {
            showMessage(
                    "Search Results",
                    "No matching items found."
            );
        }
    }

    // =====================================================
    // LOW STOCK
    // =====================================================

    @FXML
    private void handleLowStock() {

        Integer threshold = getValidPositiveInteger(
                "Low Stock",
                "Enter low-stock threshold",
                "Threshold:"
        );

        if (threshold == null) {
            return;
        }

        ArrayList<InventoryItem> lowStockItems =
                new ArrayList<>();

        for (InventoryItem item :
                inventoryManager.getItems()) {

            if (item.getQuantity() < threshold) {
                lowStockItems.add(item);
            }
        }

        inventoryTable.setItems(
                FXCollections.observableArrayList(
                        lowStockItems
                )
        );

        inventoryTable.refresh();

        statusLabel.setText(
                lowStockItems.size()
                        + " item(s) below quantity "
                        + threshold
                        + "."
        );

        if (lowStockItems.isEmpty()) {
            showMessage(
                    "Low Stock",
                    "No low-stock items found."
            );
        }
    }

    // =====================================================
    // DEALER SELECTION
    // =====================================================

    @FXML
    private void handleDealerSelection() {

        ArrayList<Dealer> dealers =
                FileHandler.loadDealers(
                        "dealers_legacy.txt"
                );

        if (dealers.size() < 4) {
            showError(
                    "Dealer Selection",
                    "At least four dealers are required."
            );
            return;
        }

        ArrayList<Dealer> selectedDealers =
                selectRandomFourDealers(dealers);

        sortDealersByLocation(selectedDealers);

        StringBuilder text =
                new StringBuilder();

        text.append(
                "RANDOMLY SELECTED DEALERS\n\n"
        );

        for (Dealer dealer :
                selectedDealers) {

            text.append("Dealer ID: ")
                    .append(dealer.getDealerId())
                    .append("\n");

            text.append("Dealer Name: ")
                    .append(dealer.getDealerName())
                    .append("\n");

            text.append("Phone: ")
                    .append(dealer.getPhoneNumber())
                    .append("\n");

            text.append("Location: ")
                    .append(dealer.getLocation())
                    .append("\n");

            text.append(
                    "------------------------------\n"
            );
        }

        TextArea area =
                new TextArea(text.toString());

        area.setEditable(false);
        area.setPrefWidth(450);
        area.setPrefHeight(350);

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Dealer Selection");
        alert.setHeaderText(
                "Four dealers sorted by location"
        );

        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    private ArrayList<Dealer> selectRandomFourDealers(
            ArrayList<Dealer> dealers) {

        ArrayList<Dealer> selected =
                new ArrayList<>();

        Random random = new Random();

        while (selected.size() < 4) {

            Dealer dealer = dealers.get(
                    random.nextInt(dealers.size())
            );

            boolean exists = false;

            for (Dealer current : selected) {

                if (current.getDealerId()
                        .equalsIgnoreCase(
                                dealer.getDealerId()
                        )) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {
                selected.add(dealer);
            }
        }

        return selected;
    }

    private void sortDealersByLocation(
            ArrayList<Dealer> dealers) {

        for (int i = 0;
             i < dealers.size() - 1;
             i++) {

            for (int j = 0;
                 j < dealers.size() - i - 1;
                 j++) {

                Dealer first =
                        dealers.get(j);

                Dealer second =
                        dealers.get(j + 1);

                if (first.getLocation()
                        .compareToIgnoreCase(
                                second.getLocation()
                        ) > 0) {

                    dealers.set(j, second);
                    dealers.set(j + 1, first);
                }
            }
        }
    }

    // =====================================================
    // POINT OF SALE
    // =====================================================

    @FXML
    private void handlePointOfSale() {

        String code = getTextInput(
                "Point of Sale",
                "Enter Part Code",
                "Part Code:"
        );

        if (code == null || code.isEmpty()) {
            return;
        }

        InventoryItem item =
                inventoryManager.findItemByCode(code);

        if (item == null) {
            showError(
                    "Item Not Found",
                    "No item found with part code "
                            + code
            );
            return;
        }

        Integer quantity = getValidPositiveInteger(
                "Point of Sale",
                "Available stock: "
                        + item.getQuantity(),
                "Quantity:"
        );

        if (quantity == null) {
            return;
        }

        if (quantity > item.getQuantity()) {
            showError(
                    "Insufficient Stock",
                    "Only "
                            + item.getQuantity()
                            + " item(s) available."
            );
            return;
        }

        cartManager.clearCart();

        cartManager.addToCart(
                code,
                quantity
        );

        double total =
                cartManager.calculateTotal();

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Checkout");

        confirmation.setHeaderText(
                item.getPartName()
        );

        confirmation.setContentText(
                "Quantity: "
                        + quantity
                        + "\nTotal: Rs. "
                        + String.format(
                        "%.2f",
                        total
                )
                        + "\n\nComplete checkout?"
        );

        Optional<ButtonType> answer =
                confirmation.showAndWait();

        if (answer.isPresent()
                && answer.get() == ButtonType.OK) {

            cartManager.checkout();

            refreshInventoryTable();

            showMessage(
                    "Checkout Complete",
                    "Sale completed successfully.\n"
                            + "Total: Rs. "
                            + String.format(
                            "%.2f",
                            total
                    )
            );

        } else {
            cartManager.clearCart();
        }
    }

    // =====================================================
    // EXIT
    // =====================================================

    @FXML
    private void handleExit() {

        FileHandler.saveInventory(
                "inventory_legacy.txt",
                inventoryManager.getItems()
        );

        Platform.exit();
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private InventoryItem getSelectedItem() {

        InventoryItem selectedItem =
                inventoryTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedItem == null) {
            selectedItem = lastSelectedItem;
        }

        if (selectedItem == null) {

            showError(
                    "No Part Selected",
                    "Click directly on a data row first, then click Update or Delete."
            );
        }

        return selectedItem;
    }

    private String getTextInput(
            String title,
            String header,
            String label) {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(label);

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return null;
        }

        return result.get().trim();
    }

    private Double getValidPrice(
            String title,
            String header) {

        while (true) {

            String text = getTextInput(
                    title,
                    header,
                    "Price:"
            );

            if (text == null) {
                return null;
            }

            try {

                double price =
                        Double.parseDouble(text);

                if (price <= 0) {

                    showError(
                            "Invalid Price",
                            "Price must be greater than zero."
                    );

                } else {
                    return price;
                }

            } catch (NumberFormatException e) {

                showError(
                        "Invalid Price",
                        "Enter numbers only."
                );
            }
        }
    }

    private Integer getValidQuantity(
            String title,
            String header) {

        while (true) {

            String text = getTextInput(
                    title,
                    header,
                    "Quantity:"
            );

            if (text == null) {
                return null;
            }

            try {

                int quantity =
                        Integer.parseInt(text);

                if (quantity < 0) {

                    showError(
                            "Invalid Quantity",
                            "Quantity cannot be negative."
                    );

                } else {
                    return quantity;
                }

            } catch (NumberFormatException e) {

                showError(
                        "Invalid Quantity",
                        "Enter a whole number only."
                );
            }
        }
    }

    private Integer getValidPositiveInteger(
            String title,
            String header,
            String label) {

        while (true) {

            String text = getTextInput(
                    title,
                    header,
                    label
            );

            if (text == null) {
                return null;
            }

            try {

                int value =
                        Integer.parseInt(text);

                if (value <= 0) {

                    showError(
                            "Invalid Number",
                            "Value must be greater than zero."
                    );

                } else {
                    return value;
                }

            } catch (NumberFormatException e) {

                showError(
                        "Invalid Number",
                        "Enter a whole number only."
                );
            }
        }
    }

    private void showMessage(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

