import java.io.*;
import java.util.*;

public class ReadSalesFile {
    public String processFile(String searchDate) {
        StringBuilder report = new StringBuilder();
        boolean isPrinting = false;

        try {
            File fn = new File("daily_sales.txt");
            BufferedReader br = new BufferedReader(new FileReader(fn));
            
            String line = br.readLine();
            while (line != null) {
                // Periksa jika tarikh sepadan
                if (line.contains(searchDate)) {
                    isPrinting = true;
                }
                
                if (isPrinting) {
                    report.append(line).append("\n");
                }
                
                // Berhenti membaca jika jumpa pemisah
                if (line.contains("------------------------------------") && isPrinting) {
                    isPrinting = false; 
                }
                
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            return "Error: No file found or error reading data.";
        }
        
        return report.length() > 0 ? report.toString() : "No sales found for date: " + searchDate;
    }
}