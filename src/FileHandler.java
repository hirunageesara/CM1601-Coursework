import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    public static ArrayList<InventoryItem> loadInventory(String fileName) {
        ArrayList<InventoryItem> items = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                InventoryItem item = parseInventoryLine(line);

                if (item != null) {
                    items.add(item);
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error loading inventory file: " + e.getMessage());
        }

        return items;
    }

    public static InventoryItem parseInventoryLine(String line) {
        try {
            line = line.replace("|", ",");
            line = line.replace(";", ",");

            String[] parts = line.split(",");

            if (parts.length < 6) {
                return null;
            }

            String code = parts[0].trim();
            String name = parts[1].trim();
            String brand = parts[2].trim();

            if (brand.equals("")) {
                brand = "Unknown";
            }

            String priceText = parts[3].trim();
            priceText = priceText.replace("Rs.", "");
            priceText = priceText.replace("Rs", "");
            priceText = priceText.replace(" ", "");

            double price = Double.parseDouble(priceText);

            int quantity = Integer.parseInt(parts[4].trim());

            String category = parts[5].trim();

            if (category.equalsIgnoreCase("engine")) {
                category = "Engine";
            } else if (category.equalsIgnoreCase("electrical")) {
                category = "Electrical";
            } else if (category.equalsIgnoreCase("bodywork")) {
                category = "Bodywork";
            } else if (category.equalsIgnoreCase("brakes")) {
                category = "Brakes";
            }

            String dateAdded = "Unknown";
            String imageFile = "no_image.png";

            if (parts.length > 6 && !parts[6].trim().equals("")) {
                dateAdded = parts[6].trim();
            }

            if (parts.length > 7 && !parts[7].trim().equals("")) {
                imageFile = parts[7].trim();
            }

            return new InventoryItem(code, name, brand, price, quantity,
                    category, dateAdded, imageFile);

        } catch (Exception e) {
            System.out.println("Skipped invalid inventory line: " + line);
            return null;
        }
    }

    public static ArrayList<Dealer> loadDealers(String fileName) {
        ArrayList<Dealer> dealers = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                Dealer dealer = parseDealerLine(line);

                if (dealer != null) {
                    dealers.add(dealer);
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error loading dealer file: " + e.getMessage());
        }

        return dealers;
    }

    public static Dealer parseDealerLine(String line) {
        try {
            line = line.replace("|", ",");
            line = line.replace(";", ",");

            String[] parts = line.split(",");

            if (parts.length < 4) {
                return null;
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            String phone = parts[2].trim();
            String location = parts[3].trim();

            if (phone.equals("")) {
                phone = "Not Available";
            }

            return new Dealer(id, name, phone, location);

        } catch (Exception e) {
            System.out.println("Skipped invalid dealer line: " + line);
            return null;
        }
    }

    public static void saveInventory(String fileName, ArrayList<InventoryItem> items) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(fileName));

            for (InventoryItem item : items) {
                pw.println(item.getPartCode() + "," +
                        item.getPartName() + "," +
                        item.getBrand() + "," +
                        item.getPrice() + "," +
                        item.getQuantity() + "," +
                        item.getCategory() + "," +
                        item.getDateAdded() + "," +
                        item.getImageFile());
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error saving inventory file: " + e.getMessage());
        }
    }
}