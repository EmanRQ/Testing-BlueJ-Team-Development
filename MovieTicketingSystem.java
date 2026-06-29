import java.util.*;
import java.io.*;
/**
 * Main Class of Movie Ticketing System
 *
 * @author (Nabil, Ahmad Adam, Uqail, Eman)
 * @version (18/06/2026)
 */
public class MovieTicketingSystem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Scanner admin = new Scanner(System.in);
        
        
        //Movie Choices
        Movie[] movieList = MovieLoader.loadMovies("movies.txt");
        
        Snack[] snackList = Snack.loadSnacks("snacks.txt");

        System.out.println("WELCOME TO NEXA MOVIES CENTRE");
        System.out.print("Do you want to start? (Y/N): ");
        String start = in.next();

        if (start.equalsIgnoreCase("Y")) {
            int choice = 0;
            do {
                //Main Menu
                System.out.print("================");
                System.out.print("\n1. Buy Ticket\n2. View Seat Map\n3. Admin Interface\n5. Exit");
                System.out.print("\nEnter Choice: ");
                choice = in.nextInt();
        
                if (choice == 1) {
                    System.out.print("Enter Name: ");
                    in.nextLine();
                    String name = in.nextLine();
                        
                    System.out.print("Enter IC: ");
                    String ic = in.next();
                        
                    System.out.print("Are you a VIP? (Y/N): ");
                    String inputVIP = in.next();
                    boolean vipStatus;

                    if (inputVIP.equalsIgnoreCase("Y")) {
                        vipStatus = true;
                    }
                    else {
                        vipStatus = false;
                    }
                    
                    saveToFile(name, ic, vipStatus);
                    
                    int birthYear = Integer.parseInt(ic.substring(0, 2));
            
                    if (birthYear <= 26) {
                        birthYear += 2000;
                    }
                    else {
                        birthYear += 1900;
                    }
                    
                    int age = 2026 - birthYear;
                    
                    Customer cust;
                    
                    if (age <= 21) {
                        cust = new StudentCustomer(name, ic, vipStatus);
                    } else if (age >= 60) {
                        cust = new SeniorCustomer(name, ic, vipStatus);
                    } else {
                        cust = new NormalCustomer(name, ic, vipStatus);
                    }
                    
                    
                    System.out.println("\nWelcome, " + cust.getName() + "!");
                    System.out.println("Category: " + cust.getCategory());
                    
                    if (vipStatus == true) {
                        System.out.println("Membership: VIP Member");
                    } else {
                        System.out.println("Membership: Non-Member");
                    }
                    
                    //call movies array
                    System.out.println("\n--- Available Movies ---");
                    for (int i = 0; i < movieList.length; i++) {
                        System.out.println((i + 1) + ". " + movieList[i].getTitle());
                    }
                    
                    System.out.print("Select movie (1-3): ");
                    int movieChoice = in.nextInt();
                    Movie selectedMovie = movieList[movieChoice - 1];
        
                    System.out.print("Quantity of tickets: ");
                    int qty = in.nextInt();
                    
                    CinemaHall selectedHall = selectedMovie.getHall();
                    
                    //seat
                    for (int i = 1; i <= qty; i++) {
                        selectedHall.displayHall(selectedMovie.getTitle());
                        System.out.println("Booking seat " + i + " of " + qty);
                        System.out.print("Enter row (1-5): ");
                        int r = in.nextInt();
                        System.out.print("Enter column (1-8): ");
                        int c = in.nextInt();
                        
                        if (!selectedHall.bookSeat(r, c)) {
                            System.out.println("Seat occupied!");
                            i--;
                        }
                    }
                    //add snack
                    System.out.print("\nWould you like to add snacks? [Y/N]: ");
                    char snackChoice = in.next().toUpperCase().charAt(0);
                    
                    double totalSnackPrice = 0;
                    String orderedSnack = "None";
                    
                    if (snackChoice == 'Y') {
                        System.out.println("\n--- SNACK MENU ---");
                        for (int i = 0; i < snackList.length; i++) {
                            if (snackList[i] != null) { 
                                System.out.println((i + 1) + ". " + snackList[i].getSnackName() + " - RM " + snackList[i].getSnackPrice());
                            }
                        }
                        
                        System.out.print("Select your snack choice (1-" + snackList.length + "): ");
                        int snackMenu = in.nextInt();
                        
                        if (snackMenu >= 1 && snackMenu <= snackList.length) {
                            Snack selectedSnack = snackList[snackMenu - 1];
                            totalSnackPrice = selectedSnack.getSnackPrice();
                            orderedSnack = selectedSnack.getSnackName();
                        } else {
                            System.out.println("Invalid choice, no snack added.");
                        }
                    }
                    
                    //total
                    double priceBeforeDiscount = selectedMovie.calculateTotal(qty);
                    double finalPrice = priceBeforeDiscount - (cust.getDiscount() * qty) - (cust.getDiscountVIP() * qty);
                    double discountTotal = priceBeforeDiscount - finalPrice;
                    double tax = finalPrice * 0.06;
                    double grandTotal = finalPrice + tax + totalSnackPrice;
                    
                    //receipt
                    System.out.println("\n--- RECEIPT ---");
                    System.out.println("Movie: " + selectedMovie.getTitle());
                    System.out.println("Tickets: " + qty + " pcs");
                    System.out.println("Tickets Price: RM " + selectedMovie.getBasePrice());
                    System.out.println("Snack: " + orderedSnack + " (RM " + totalSnackPrice + ")");
                    System.out.println("Discount: RM " + discountTotal);
                    System.out.printf("Grand Total (Inc. 6%% Tax): RM %.2f\n", grandTotal);
        
                    saveToFile(selectedMovie.getTitle(), qty , orderedSnack, totalSnackPrice, grandTotal, selectedMovie.getBasePrice(), discountTotal);
                }
                if (choice == 2) {
                    System.out.println("\n--- VIEW SEAT AVAILABILITY ---");
                    for (int i = 0; i < movieList.length; i++) {
                            System.out.println((i + 1) + ". " + movieList[i].getTitle());
                    }
                    System.out.print("Select movie: ");
                    int choice3 = in.nextInt();
                        
                    movieList[choice3 - 1].getHall().displayHall(movieList[choice3 - 1].getTitle());
                }
                if (choice == 3) {
                    System.out.print("Username: ");
                    String username = admin.nextLine();
                    System.out.print("Password: ");
                    String password = admin.nextLine(); 
                    
                    if(username.equals("admin") && (password.equals("admin"))) {
                        System.out.println("Welcome Admin!!");
                        //Store and edit exiting data of an array of object
                        do{
                            System.out.print("================");
                            System.out.print("\n1. Read Daily Sales File\n2. Read & Update Movie Prices\n3. Read & Update Snacks Prices\n5. Exit");
                            System.out.print("\nEnter Choice: ");
                            choice = in.nextInt();
                            
                            if (choice == 1) {
                                System.out.print("Enter Date (Example: Jun 23): ");
                                String date = admin.nextLine(); 
                                
                                ReadSalesFile dailyReader = new ReadSalesFile();
                                dailyReader.processFile(date);
                            }
                            if (choice == 2) {
                                System.out.println("******************");
                                //Display movie list with price from file
                                for (int i = 0; i < movieList.length; i++) {
                                    System.out.println((i + 1) + ". " + movieList[i].getTitle() + " - RM " + movieList[i].getBasePrice());
                                }
                                
                                System.out.print("Movie (1-" + movieList.length + "): ");
                                int movieNum = in.nextInt();
                                
                                if (movieNum >= 1 && movieNum <= movieList.length) {
                                    int movieIdx = movieNum - 1;
                                    System.out.print("New Price: RM ");
                                    double newPrice = in.nextDouble();
                                    
                                    movieList[movieIdx].setBasePrice(newPrice);
                                    MovieLoader.saveMovies(movieList, "movies.txt");
                                    System.out.println("Price Successfully Updated!!!");
                                } else {
                                    System.out.println("Error, Invalid movie selection");
                                }
                            }
                            if (choice == 3) {
                                int adminSnackChoice = 0;
                                
                                do {
                                    System.out.println("\n--- SNACK MANAGEMENT ---");
                                    System.out.println("1. Update Snack Price");
                                    System.out.println("2. Add New Snack");
                                    System.out.println("3. Remove Snack");
                                    System.out.println("5. Exit");
                                    System.out.print("Enter Choice: ");
                                    adminSnackChoice = in.nextInt();
                            
                                    if (adminSnackChoice == 1) {
                                        for (int i = 0; i < snackList.length; i++) {
                                            if (snackList[i] != null) {
                                                System.out.println((i + 1) + ". " + snackList[i].getSnackName() + " - RM " + snackList[i].getSnackPrice());
                                            }
                                        }
                                        System.out.print("Choose Snack to update (1-" + snackList.length + "): ");
                                        int sNum = in.nextInt();
                                        
                                        if (sNum >= 1 && sNum <= snackList.length && snackList[sNum - 1] != null) {
                                            int sIdx = sNum - 1;
                                            System.out.print("New Price: RM ");
                                            double newPrice = in.nextDouble();
                                            snackList[sIdx] = new Snack(snackList[sIdx].getSnackName(), newPrice);
                                            Snack.saveSnacks(snackList, "snacks.txt");
                                            System.out.println("Price updated!");
                                        } else {
                                            System.out.println("Invalid selection!");
                                        }
                                    }
                                    else if (adminSnackChoice == 2) {
                                        int currentCount = 0;
                                        for (int i = 0; i < snackList.length; i++) {
                                            if (snackList[i] != null) {
                                                currentCount++;
                                            }
                                        }
                            
                                        if (currentCount < snackList.length) {
                                            in.nextLine();
                                            System.out.print("New Snack Name: ");
                                            String newName = in.nextLine();
                                            System.out.print("New Price: RM ");
                                            double newPrice = in.nextDouble();
                                            
                                            snackList[currentCount] = new Snack(newName, newPrice);
                                            Snack.saveSnacks(snackList, "snacks.txt");
                                            System.out.println("Snack added!");
                                        }
                                        else {
                                            System.out.println("Snack list full!");
                                        }
                                    }
                                    else if (adminSnackChoice == 3) {
                                        for (int i = 0; i < snackList.length; i++) {
                                            if (snackList[i] != null) {
                                                System.out.println((i + 1) + ". " + snackList[i].getSnackName());
                                            }
                                        }
                                        
                                        System.out.print("Choose Snack: ");
                                        int sNum = in.nextInt();
                                        
                                        if (sNum >= 1 && sNum <= snackList.length && snackList[sNum - 1] != null) {
                                            snackList[sNum - 1] = null;
                                        
                                            Snack.saveSnacks(snackList, "snacks.txt");
                                            System.out.println("Snack Removed Successfully!");
                                        } else {
                                            System.out.println("Invalid selection.");
                                        }
                                    }
                                } while (adminSnackChoice != 5);
                            }
                        }while (choice != 5);
                    }
                }
            } while (choice != 5);
            System.out.print("\fBye-Bye!!<3");
        }
    }

    public static void saveToFile(String name, String ic, boolean vipStatus) {
        try {
            FileWriter fw = new FileWriter("daily_sales.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            
            Date currentDate = new Date();
            
            
            pw.println("\n\n---------------------------------");
            pw.println("Date: " + currentDate.toString());
            pw.println("Name: " + name);
            pw.println("IC Number: " + ic);
            pw.println("VIP Status: " + vipStatus);
            pw.println("*******************");
            
            pw.close();
        } 
        catch (IOException e) {
            System.out.println("Error");
        }
    }
    
    public static void saveToFile(String movie, int qty, String orderedSnack, double totalSnackPrice, double grandTotal, double ticketPrice, double discountTotal) {
        try {
            FileWriter fw = new FileWriter("daily_sales.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            
            
            pw.println("Movie: " + movie);
            pw.println("Quantity: " + qty);
            pw.println("Ticket Price: RM " + ticketPrice);
            pw.println("Snack: " + orderedSnack + " (RM " + totalSnackPrice + ")");
            pw.println("Discount: RM " + discountTotal);
            pw.println("Total Bill (inc Tax): RM " + grandTotal);
            pw.println("---------------------------------");
            
            pw.close();
        } 
        catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }
}