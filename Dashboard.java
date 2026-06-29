import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Dashboard extends JFrame implements ActionListener {
    private JButton btnBuy, btnSeat, btnAdmin, btnExit;
    Movie[] movieList = MovieLoader.loadMovies("movies.txt");
    Snack[] snackList = Snack.loadSnacks("snacks.txt");
    

    public Dashboard() {
        movieList = MovieLoader.loadMovies("movies.txt");

        setTitle("NEXA Movie Ticketing System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel header = new JLabel("NEXA MOVIES DASHBOARD", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        add(header);

        btnBuy = new JButton("1. Buy Ticket");
        btnSeat = new JButton("2. View Seat Map");
        btnAdmin = new JButton("3. Admin Interface");
        btnExit = new JButton("5. Exit");

        btnBuy.addActionListener(this);
        btnSeat.addActionListener(this);
        btnAdmin.addActionListener(this);
        btnExit.addActionListener(this);

        add(btnBuy); add(btnSeat); add(btnAdmin); add(btnExit);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBuy) {
            handleBuy();
        } else if (e.getSource() == btnSeat) {
            handleSeat();
        } else if (e.getSource() == btnAdmin) {
            handleAdmin();
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    private void handleBuy() {
        JTextField nameField = new JTextField();
        JTextField icField = new JTextField();
        Object[] msg = {"Name:", nameField, "IC:", icField};
        
        if (JOptionPane.showConfirmDialog(this, msg, "Customer Info", JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String ic = icField.getText();
            
            // 1. Bina objek Customer untuk logik diskaun
            int year = Integer.parseInt(ic.substring(0, 2));
            int age = 2026 - (year <= 26 ? year + 2000 : year + 1900);
            Customer cust = (age <= 21) ? new StudentCustomer(name, ic, false) : 
                            (age >= 60) ? new SeniorCustomer(name, ic, false) : new NormalCustomer(name, ic, false);
            
            // 2. Pilih Movie
            String[] mList = new String[movieList.length];
            for(int i = 0; i < movieList.length; i++) mList[i] = movieList[i].getTitle();
            String sel = (String)JOptionPane.showInputDialog(this, "Select Movie:", "Booking", 
                         JOptionPane.QUESTION_MESSAGE, null, mList, mList[0]);
            
            if (sel != null) {
                String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
                if (qtyStr != null) {
                    int qty = Integer.parseInt(qtyStr);
                    Movie selectedMovie = null;
                    for(Movie m : movieList) { if(m.getTitle().equals(sel)) selectedMovie = m; }
                    
                    // 3. Panggil handleSelectSeats. 
                    // Kita pindahkan panggil selectSnacks ke DALAM handleSelectSeats 
                    // supaya ia hanya jalan SELEPAS kerusi dipilih.
                    handleSelectSeats(selectedMovie, qty, cust);
                }
            }
        }
    }

    private void handleSeat() {
        String[] mList = new String[movieList.length];
        for(int i = 0; i < movieList.length; i++) mList[i] = movieList[i].getTitle();
        
        String sel = (String)JOptionPane.showInputDialog(this, "Select Movie to View Seats:", "Seat Map", 
                     JOptionPane.QUESTION_MESSAGE, null, mList, mList[0]);
        
        if (sel != null) {
            // Find selected movie
            Movie selectedMovie = null;
            for(Movie m : movieList) {
                if(m.getTitle().equals(sel)) selectedMovie = m;
            }
            
            // Open a dialog to show the seat grid
            JDialog seatDialog = new JDialog(this, "Seat Map: " + sel, true);
            seatDialog.setLayout(new BorderLayout());
            
            JPanel seatGrid = new JPanel(new GridLayout(5, 8, 5, 5));
            CinemaHall hall = selectedMovie.getHall();
            
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 8; j++) {
                    JButton btn = new JButton((i + 1) + "-" + (j + 1));
                    
                    // Set color based on booking status
                    if (hall.isBooked(i, j)) {
                        btn.setBackground(Color.RED);
                        btn.setEnabled(false); // Booked seats are not selectable
                    } else {
                        btn.setBackground(Color.GREEN);
                    }
                    seatGrid.add(btn);
                }
            }
            
            seatDialog.add(new JLabel("Current Seat Status", JLabel.CENTER), BorderLayout.NORTH);
            seatDialog.add(seatGrid, BorderLayout.CENTER);
            
            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(e -> seatDialog.dispose());
            seatDialog.add(closeBtn, BorderLayout.SOUTH);
            
            seatDialog.pack();
            seatDialog.setLocationRelativeTo(this);
            seatDialog.setVisible(true);
        }
    }
    
    private void handleSelectSeats(Movie movie, int qty, Customer cust) {
        JDialog seatDialog = new JDialog(this, "Select " + qty + " Seats", true);
        seatDialog.setLayout(new BorderLayout());
        
        JPanel seatGrid = new JPanel(new GridLayout(5, 8, 5, 5));
        CinemaHall hall = movie.getHall();
        JToggleButton[][] buttons = new JToggleButton[5][8];
        final int[] selectedCount = {0};
    
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                final int row = i; final int col = j;
                buttons[i][j] = new JToggleButton((i + 1) + "-" + (j + 1));
                
                if (hall.isBooked(i, j)) {
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setBackground(Color.RED);
                } else {
                    buttons[i][j].setBackground(Color.GREEN);
                    buttons[row][col].addActionListener(e -> {
                        JToggleButton btn = (JToggleButton) e.getSource();
                        if (btn.isSelected()) {
                            if (selectedCount[0] < qty) { selectedCount[0]++; btn.setBackground(Color.YELLOW);
                            } else { btn.setSelected(false); JOptionPane.showMessageDialog(seatDialog, "Max seats reached!"); }
                        } else { selectedCount[0]--; btn.setBackground(Color.GREEN); }
                    });
                }
                seatGrid.add(buttons[i][j]);
            }
        }
    
        JButton confirmBtn = new JButton("Confirm Seats");
        confirmBtn.addActionListener(e -> {
            if (selectedCount[0] == qty) {
                // 1. Update Kerusi
                for (int i=0; i<5; i++) {
                    for (int j=0; j<8; j++) {
                        if (buttons[i][j].isSelected()) hall.setSeatBooked(i, j);
                    }
                }
                seatDialog.dispose();
                
                // 2. Pilih Snack
                selectSnacks(movie, qty, cust);
            } else {
                JOptionPane.showMessageDialog(this, "Please select exactly " + qty + " seats.");
            }
        });
        
        seatDialog.add(seatGrid, BorderLayout.CENTER);
        seatDialog.add(confirmBtn, BorderLayout.SOUTH);
        seatDialog.pack();
        seatDialog.setLocationRelativeTo(this);
        seatDialog.setVisible(true);
    }
    
    private void selectSnacks(Movie movie, int qty, Customer cust) {
        String[] sList = new String[snackList.length];
        for (int i = 0; i < snackList.length; i++) {
            if (snackList[i] != null) 
                sList[i] = snackList[i].getSnackName() + " (RM" + snackList[i].getSnackPrice() + ")";
        }
        
        String selSnack = (String)JOptionPane.showInputDialog(this, "Select your snack:", "Snack Selection", 
                         JOptionPane.QUESTION_MESSAGE, null, sList, sList[0]);
        
        // Logik Pengiraan
        double ticketPrice = movie.getBasePrice() * qty;
        double snackPrice = 0;
        String snackName = "None";
        
        if (selSnack != null) {
            // Cari snack dalam snackList untuk ambil harga
            for(Snack s : snackList) {
                if (s != null && selSnack.contains(s.getSnackName())) {
                    snackPrice = s.getSnackPrice();
                    snackName = s.getSnackName();
                }
            }
        }
        
        double discount = 0; // Tambah logik diskaun ikut Customer class anda
        double totalBeforeTax = ticketPrice + snackPrice - discount;
        double grandTotal = totalBeforeTax * 1.06; // 6% Tax
        
        // Bina String Resit
        String receipt = String.format(
            "--- NEXA RECEIPT ---\n" +
            "Movie         : %s\n" +
            "Tickets (%d)   : RM %.2f\n" +
            "Snack         : %s (RM %.2f)\n" +
            "Discount      : RM %.2f\n" +
            "--------------------\n" +
            "Grand Total   : RM %.2f\n" +
            "(Incl. 6%% Tax)",
            movie.getTitle(), qty, ticketPrice, snackName, snackPrice, discount, grandTotal
        );
        
        saveReceiptToFile(receipt);
        
        // Papar dalam JTextArea
        JTextArea resitArea = new JTextArea(receipt);
        resitArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resitArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(resitArea), "Booking Confirmed!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleAdmin() {
        // 1. Autentikasi
        String pass = JOptionPane.showInputDialog(this, "Enter Password:");
        if (!"admin".equals(pass)) {
            JOptionPane.showMessageDialog(this, "Invalid Password.");
            return;
        }
    
        // 2. Menu Admin Utama
        String[] options = {"Read Daily Sales", "Update Movie Price", "Manage Snacks", "Exit"};
        int choice;
        
        do {
            choice = JOptionPane.showOptionDialog(this, "Admin Dashboard", "Admin Interface",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    
            if (choice == 0) { // Read Sales
                String date = JOptionPane.showInputDialog(this, "Enter Date (e.g., 2026-06-30):");
                if (date != null) {
                    // Panggil method dan dapatkan String report
                    String salesData = new ReadSalesFile().processFile(date);
                    
                    // Paparkan dalam tetingkap yang boleh scroll
                    JTextArea textArea = new JTextArea(salesData);
                    textArea.setRows(15);
                    textArea.setColumns(30);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
                                                 "Sales Report: " + date, JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if (choice == 1) { // Update Movie Price
                updateMoviePrice();
            } 
            else if (choice == 2) { // Manage Snacks
                manageSnacks();
            }
        } while (choice != 3 && choice != -1); // Exit atau tutup dialog
    }
    
    private void updateMoviePrice() {
        String[] mList = new String[movieList.length];
        for(int i = 0; i < movieList.length; i++) mList[i] = movieList[i].getTitle();
        
        String sel = (String)JOptionPane.showInputDialog(this, "Select Movie to Update Price:", "Movies", 
                     JOptionPane.QUESTION_MESSAGE, null, mList, mList[0]);
        
        if (sel != null) {
            String newPriceStr = JOptionPane.showInputDialog(this, "Enter new price for " + sel + ":");
            if (newPriceStr != null) {
                double price = Double.parseDouble(newPriceStr);
                for(Movie m : movieList) {
                    if(m.getTitle().equals(sel)) {
                        m.setBasePrice(price);
                        MovieLoader.saveMovies(movieList, "movies.txt");
                        JOptionPane.showMessageDialog(this, "Price Updated!");
                    }
                }
            }
        }
    }
    
    private void manageSnacks() {
        String[] options = {"Update Price", "Add New Snack", "Remove Snack", "Back"};
        int choice = JOptionPane.showOptionDialog(this, "Snack Management", "Admin",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
        if (choice == 0) { // Update Price
            updateSnackPrice();
        } else if (choice == 1) { // Add Snack
            addSnack();
        } else if (choice == 2) { // Remove Snack
            removeSnack();
        }
    }
    
    public void recordSales(String data) {
        // Parameter 'true' sangat penting supaya data lama tidak hilang!
        try (PrintWriter pw = new PrintWriter(new FileWriter("daily_sales.txt", true))) {
            pw.println(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving sales: " + e.getMessage());
        }
    }

    private void saveReceiptToFile(String receiptContent) {
        // Gunakan 'true' untuk APPEND (tambah ke hujung fail tanpa padam data lama)
        try (PrintWriter pw = new PrintWriter(new FileWriter("daily_sales.txt", true))) {
            String dateHeader = "--- DATE: " + java.time.LocalDate.now().toString() + " ---\n";
            pw.println(dateHeader);
            pw.println(receiptContent);
            pw.println("------------------------------------");
            pw.println(); // Ruang kosong antara resit
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to sales file: " + e.getMessage());
        }
    }
    
    public void processFile(String dateInput) {
        try (Scanner in = new Scanner(new File("daily_sales.txt"))) {
            boolean found = false;
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // Cari tarikh yang sepadan
                if (line.contains("DATE: " + dateInput)) {
                    found = true;
                    // Paparkan resit sehingga bertemu pemisah
                    while (in.hasNextLine()) {
                        String receiptLine = in.nextLine();
                        if (receiptLine.contains("---")) break;
                        System.out.println(receiptLine);
                    }
                }
            }
            if (!found) System.out.println("No sales found for this date.");
        } catch (FileNotFoundException e) {
            System.out.println("Sales file not found!");
        }
    }
    
    private void updateSnackPrice() {
        String[] sList = new String[snackList.length];
        int count = 0;
        for (int i = 0; i < snackList.length; i++) {
            if (snackList[i] != null) {
                sList[count++] = (i + 1) + ". " + snackList[i].getSnackName();
            }
        }
        
        String sel = (String)JOptionPane.showInputDialog(this, "Select Snack to update:", "Update Price", 
                     JOptionPane.QUESTION_MESSAGE, null, sList, sList[0]);
        
        if (sel != null) {
            int index = Integer.parseInt(sel.split("\\.")[0]) - 1;
            String newPriceStr = JOptionPane.showInputDialog(this, "New Price for " + snackList[index].getSnackName() + ":");
            if (newPriceStr != null) {
                double newPrice = Double.parseDouble(newPriceStr);
                snackList[index] = new Snack(snackList[index].getSnackName(), newPrice);
                Snack.saveSnacks(snackList, "snacks.txt");
                JOptionPane.showMessageDialog(this, "Price Updated Successfully!");
            }
        }
    }
    
    private void addSnack() {
        int emptyIdx = -1;
        for (int i = 0; i < snackList.length; i++) {
            if (snackList[i] == null) {
                emptyIdx = i;
                break;
            }
        }
        
        if (emptyIdx != -1) {
            String name = JOptionPane.showInputDialog(this, "New Snack Name:");
            String priceStr = JOptionPane.showInputDialog(this, "Price:");
            if (name != null && priceStr != null) {
                snackList[emptyIdx] = new Snack(name, Double.parseDouble(priceStr));
                Snack.saveSnacks(snackList, "snacks.txt");
                JOptionPane.showMessageDialog(this, "Snack Added!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Snack list is full!");
        }
    }
    
    private void removeSnack() {
        String[] sList = new String[snackList.length];
        int count = 0;
        for (int i = 0; i < snackList.length; i++) {
            if (snackList[i] != null) {
                sList[count++] = (i + 1) + ". " + snackList[i].getSnackName();
            }
        }
        
        String sel = (String)JOptionPane.showInputDialog(this, "Select Snack to remove:", "Remove Snack", 
                     JOptionPane.QUESTION_MESSAGE, null, sList, sList[0]);
        
        if (sel != null) {
            int index = Integer.parseInt(sel.split("\\.")[0]) - 1;
            snackList[index] = null;
            Snack.saveSnacks(snackList, "snacks.txt");
            JOptionPane.showMessageDialog(this, "Snack Removed!");
        }
    }
    
    public static void main(String[] args) {
        new Dashboard();
    }
}