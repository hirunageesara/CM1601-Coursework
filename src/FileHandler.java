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

        }
    }
}
