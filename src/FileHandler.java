import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    public static ArrayList<InventoryItem> loadInventory(String fileName){
        ArrayList<InventoryItem> items=new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader((fileName));
            String line;
            while ((line = br.readLine() != null)){
                if (item !=null){
                    items.add(item);

                }
            }
            br.close();
        }catch (IOException e){
            system.out.println("error loading inventory file:"+e.getMessage())
        }
        return items
    }
    public static InventoryItem parseInventoryLine(String Line){
        try{
            line =line.replace("|",",");
            line =line.replace(";",",");

            String[] parts= line.split(",");
            if (parts.length<6){
                return null;

            }
            String[] code = parts[0].trim();
            String[] name = parts[1].trim();
            String[] brand = parts[2].trim();

            if (brand.equals("")){
                brand = "Unknown";

            }
            String priceText = parts[3].trim();
            priceText =priceText.replace("Rs","");
            priceText =priceText.replace("Rs","");
            priceText =priceText.replace(" ","");

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(parts[4].trim());
            String category = parts[5].trim();
            if (category.equalsIgnoreCase("engine")){
                category = "Engine";
            }
             else if (category.equalsIgnoreCase("electical")){
                  category = "Electrical";
             }
             else if (category.equalsIgnoreCase("bodywork")){
                 category = "Body work";
             }
             else if (category.equalsIgnoreCase("brakes")){
                 category = "Brakes";
            }
             String dataAdded ="Unknown";
             String imageFile ="no image.png";

             if (parts.length>6 &&!parts[6].trim().equals("")){
                 dataAdded = parts[6].trim();
             }
             if (parts.length>7 && !parts[7].trim().equals("")){
                 imageFile = parts[6].trim();
             }
             return new InventoryItem(code,name,brand,price,quantity,
                     category,dataAdded,imageFile);

        }catch (Exception e){
            System.out.println("Skipped invalid inventory line:" + line );
            return null;

        }
    }
    public static ArrayList<Dealer> loadDealers(String fileName){
        ArrayList<Dealer> dealers = new ArrayList<>();

        try{
            BufferedReader br= new BufferedReader(new FileReader(fileName));
            String line;
            while ((line=br.readLine()) !=null){
                Dealer dealer = parseInventoryLine(line);
                if (dealer!=null){
                    dealers.add(dealer);
                }
            }
            br.close();
        }catch ((IOException e){
            System.out.println("Error loading dealer file:"+e.getMassage());
        }
        return dealers;

    }
    public static Dealer parsDealerLine(String line){
        try{
            line = line.replace("|",",");
            line = line.replace(";",",");

            String[] parts = line.split(",");

            if(parts.length<4){
                return null;

            }
            String id = parts[0].trim();
            String name = parts[1].trim();
            String phone =parts[2].trim();

            if (phone.equals("")){
                phone ="not availabale";
            }
            return new Dealer(id,name,phone,location);
        }
    }
}
