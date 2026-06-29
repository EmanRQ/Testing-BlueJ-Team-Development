/**
 * Write a description of class CinemaHall here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CinemaHall {
    private char[][] seats;

    public CinemaHall() {
        seats = new char[5][8];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                seats[i][j] = 'O';
            }
        }
    }

    public void displayHall(String movieName) {
        System.out.println("\n--- SEAT MAP FOR: " + movieName + " ---");
        System.out.println("      [FRONT / SCREEN]");
        for (int i = 0; i < 5; i++) {
            System.out.print("Row " + (i + 1) + "  - ");
            for (int j = 0; j < 8; j++) {
                System.out.print("[" + seats[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.print("Column - (1) (2) (3) (4) (5) (6) (7) (8)");
        System.out.println("\nNote: [O] Available  [X] Booked\n");
    }

    public boolean bookSeat(int r, int c) {
        if (seats[r-1][c-1] == 'X') return false;
        seats[r-1][c-1] = 'X';
        return true;
    }
    
    public String getHallLayout() {
        StringBuilder sb = new StringBuilder();
        sb.append("      [FRONT / SCREEN]\n");
        for (int i = 0; i < 5; i++) {
            sb.append("Row ").append(i + 1).append("  - ");
            for (int j = 0; j < 8; j++) {
                sb.append("[").append(seats[i][j]).append("] ");
            }
            sb.append("\n");
        }
        sb.append("\nColumn - (1)(2)(3)(4)(5)(6)(7)(8)\n");
        sb.append("Note: [O] Available  [X] Booked");
        return sb.toString();
    }
    
    public boolean isBooked(int row, int col) {
        return seats[row][col] == 'X'; // Andaian 'X' adalah kerusi ditempah
    }
    
    public void setSeatBooked(int row, int col) {
        this.seats[row][col] = 'X'; // Menandakan kerusi ditempah
    }
}