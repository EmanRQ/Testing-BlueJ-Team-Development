public class Movie {
    private String title;
    private double basePrice;
    private CinemaHall hall;

    public Movie(String title, double basePrice) {
        this.title = title;
        this.basePrice = basePrice;
        this.hall = new CinemaHall();
    }
    
    public String getTitle() { 
        return title; 
    }

    public double getBasePrice() { 
        return basePrice; 
    }

    public void setBasePrice(double basePrice) { 
        this.basePrice = basePrice; 
    }

    public CinemaHall getHall() { 
        return hall; 
    }
    
    public double calculateTotal(int quantity) {
        return basePrice * quantity;
    }
}