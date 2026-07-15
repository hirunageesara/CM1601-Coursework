import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextInputDialog;

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

    @FXML
    public void initialize() {

        titleLabel.setText("Malabe Spares Depot Inventory System");

        // Connect each table column to InventoryItem getter methods.
        colCode.setCellValueFactory(
                new PropertyValueFactory<>("partCode"));

        colName.setCellValueFactory(
                new PropertyValueFactory<>("partName"));

        colBrand.setCellValueFactory(
                new PropertyValueFactory<>("brand"));

        colPrice.setCellValueFactory(
                new PropertyValueFactory<>("price"));

        colQuantity.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        colCategory.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        colDateAdded.setCellValueFactory(
                new PropertyValueFactory<>("dateAdded"));

        // Load existing inventory when the application starts.
        loadInventory();
    }

    private void loadInventory() {

        inventoryItems =
                FileHandler.loadInventory("inventory_legacy.txt");

        inventoryManager =
                new InventoryManager(inventoryItems);

        statusLabel.setText(
                inventoryItems.size() + " inventory items loaded.");
    }

    private void refreshInventoryTable() {

        inventoryTable.setItems(
                FXCollections.observableArrayList(
                        inventoryManager.getItems()
                )
        );

        inventoryTable.refresh();

        statusLabel.setText(
                inventoryManager.getTotalParts()
                        + " parts displayed. Total value: Rs. "
                        + String.format(
                        "%.2f",
                        inventoryManager.getTotalValue()
                )
        );
    }

    @FXML
    private void handleViewInventory() {

        // Uses your own manual Bubble Sort method.
        inventoryManager.sortByCategoryAndCode();

        refreshInventoryTable();
    }

    @FXML
    private void handleAddPart() {

        TextInputDialog codeDialog = new TextInputDialog();
        codeDialog.setTitle("Add Part");
        codeDialog.setHeaderText("Enter Part Code");
        codeDialog.setContentText("Part Code:");

        String partCode = codeDialog.showAndWait().orElse("").trim();

        if (partCode.isEmpty()) {
            showError("Invalid Input", "Part code cannot be empty.");
            return;
        }

        if (inventoryManager.findItemByCode(partCode) != null) {
            showError("Duplicate Code", "This part code already exists.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Part");
        nameDialog.setHeaderText("Enter Part Name");
        nameDialog.setContentText("Part Name:");

        String partName = nameDialog.showAndWait().orElse("").trim();

        if (partName.isEmpty()) {
            showError("Invalid Input", "Part name cannot be empty.");
            return;
        }

        TextInputDialog brandDialog = new TextInputDialog();
        brandDialog.setTitle("Add Part");
        brandDialog.setHeaderText("Enter Brand");
        brandDialog.setContentText("Brand:");

        String brand = brandDialog.showAndWait().orElse("").trim();

        if (brand.isEmpty()) {
            showError("Invalid Input", "Brand cannot be empty.");
            return;
        }

        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add Part");
        priceDialog.setHeaderText("Enter Price");
        priceDialog.setContentText("Price:");

        String priceText = priceDialog.showAndWait().orElse("").trim();

        double price;

        try {
            price = Double.parseDouble(priceText);

            if (price <= 0) {
                showError("Invalid Input", "Price must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Price must be a valid number.");
            return;
        }

        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add Part");
        quantityDialog.setHeaderText("Enter Quantity");
        quantityDialog.setContentText("Quantity:");

        String quantityText = quantityDialog.showAndWait().orElse("").trim();

        int quantity;

        try {
            quantity = Integer.parseInt(quantityText);

            if (quantity < 0) {
                showError("Invalid Input", "Quantity cannot be negative.");
                return;
            }

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Quantity must be a whole number.");
            return;
        }

        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add Part");
        categoryDialog.setHeaderText("Enter Category");
        categoryDialog.setContentText(
                "Category (Engine, Electrical, Bodywork or Brakes):"
        );

        String category = categoryDialog.showAndWait().orElse("").trim();

        if (category.isEmpty()) {
            showError("Invalid Input", "Category cannot be empty.");
            return;
        }

        String dateAdded = java.time.LocalDate.now().toString();
        String imageFile = "no_image.png";

        InventoryItem newItem = new InventoryItem(
                partCode,
                partName,
                brand,
                price,
                quantity,
                category,
                dateAdded,
                imageFile
        );

        inventoryManager.addItem(newItem);

        refreshInventoryTable();

        showMessage(
                "Part Added",
                "Part added successfully."
        );
    }

    @FXML
    private void handleUpdatePart() {

        InventoryItem selectedItem =
                inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError(
                    "No Part Selected",
                    "Please select an inventory item from the table."
            );
            return;
        }

        showMessage(
                "Update Part",
                "Selected part: "
                        + selectedItem.getPartCode()
                        + " - "
                        + selectedItem.getPartName()
        );
    }

    @FXML
    private void handleDeletePart() {

        InventoryItem selectedItem =
                inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError(
                    "No Part Selected",
                    "Please select an inventory item before deleting."
            );
            return;
        }

        inventoryManager.deleteItem(
                selectedItem.getPartCode()
        );

        refreshInventoryTable();

        showMessage(
                "Part Deleted",
                selectedItem.getPartCode()
                        + " was deleted successfully."
        );
    }

    @FXML
    private void handleSearchParts() {

        showMessage(
                "Search Parts",
                "The Search Parts form will be implemented next."
        );
    }

    @FXML
    private void handleLowStock() {

        ArrayList<InventoryItem> lowStockItems =
                new ArrayList<>();

        for (InventoryItem item :
                inventoryManager.getItems()) {

            if (item.getQuantity() < 10) {
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
                        + " low-stock items displayed."
        );
    }

    @FXML
    private void handleDealerSelection() {

        showMessage(
                "Dealer Selection",
                "The Dealer Selection page will be implemented next."
        );
    }

    @FXML
    private void handlePointOfSale() {

        showMessage(
                "Point of Sale",
                "The Point of Sale page will be implemented next."
        );
    }

    @FXML
    private void handleExit() {

        FileHandler.saveInventory(
                "inventory_legacy.txt",
                inventoryManager.getItems()
        );

        Platform.exit();
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