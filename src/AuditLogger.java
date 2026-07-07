import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditLogger {

    public static void log(String action, String itemCode, int quantity) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("audit_log.txt", true));

            pw.println(LocalDateTime.now() +
                    " | Action: " + action +
                    " | Item Code: " + itemCode +
                    " | Quantity: " + quantity);

            pw.close();

        } catch (IOException e) {
            System.out.println("Error writing audit log: " + e.getMessage());
        }
    }
}