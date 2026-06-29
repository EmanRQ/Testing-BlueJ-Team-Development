import java.util.*;
import java.io.*;
public class NEXA_Main 
{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        NEXA_Team[] members = new NEXA_Team[4];
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("Members.txt"));
            for (int i = 0; i < 4; i++) {
                String line = br.readLine();
                if (line != null) {
                    String[] parts = line.split(", ");
                    members[i] = new NEXA_Team(parts[0], parts[1]);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        System.out.println("Hello Visitor ^ ^ ");
        System.out.println("               >  ");
        
        System.out.println("Before that if you can see this line means Team Development Features works!!! >-<");
        
        System.out.print("\nAre you NEXA members? [Y/N]: ");
        char answer = in.next().toUpperCase().charAt(0);
        in.nextLine();

        if(answer == 'Y') {
            System.out.print("Please Enter Your Name: ");
            String inputName = in.nextLine();
            boolean found = false;

            for (NEXA_Team m : members) {
                if (m != null && m.getName().equalsIgnoreCase(inputName)) {
                    System.out.println("Welcome " + m.getName() + "!! " + "\nYour role is: " + m.getRole());
                    found = true;
                    break;
                }
            }
            if (!found) System.out.println("Member not found in database.");
        } else {
            System.out.println("Get Out!!!");
        }
        in.close();
    }
}