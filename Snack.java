import java.io.*;
import java.util.*;

public class Snack {
    private String snackName;
    private double snackPrice;

    public Snack(String name, double price) {
        this.snackName = name;
        this.snackPrice = price;
    }

    public static Snack[] loadSnacks(String filename) {
        Snack[] snacks = new Snack[10];
        int i = 0;
        try (Scanner in = new Scanner(new File(filename))) {
            while (in.hasNextLine() && i < snacks.length) {
                String line = in.nextLine();
                if (line.equals("-")) {
                    snacks[i] = null;
                } else {
                    String[] parts = line.split(",");
                    snacks[i] = new Snack(parts[0], Double.parseDouble(parts[1]));
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error Reading File!" + e.getMessage());
        }
        while(i < 10) {
            snacks[i] = null; i++;
        }
        return snacks;
    }

    public static void saveSnacks(Snack[] snacks, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Snack s : snacks) {
                if (s == null) {
                    pw.println("-");
                } else {
                    pw.println(s.getSnackName() + "," + s.getSnackPrice());
                }
            }
        } catch (IOException e) {
            System.out.println("Error Saving File!: " + e.getMessage());
        }
    }

    public String getSnackName() {
        return snackName;
    }
    public double getSnackPrice() {
        return snackPrice;
    }
}