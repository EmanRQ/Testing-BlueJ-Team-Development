public abstract class Customer {
    protected String name;
    protected String ic;
    protected int age;
    protected boolean isVIP;

    public Customer(String name, String ic, boolean isVIP) {
        this.name = name;
        this.ic = ic;
        this.isVIP = isVIP;
        this.age = calculateAge(ic);
    }

    private int calculateAge(String ic) {
        int birthYear = Integer.parseInt(ic.substring(0, 2));
            
            if (birthYear <= 26) {
                birthYear += 2000;
            }
            else {
                birthYear += 1900;
            }
    
        return this.age = 2026 - birthYear;
    }

    public abstract double getDiscount();
    public abstract String getCategory();
    
    public double getDiscountVIP() {
        double discount;
        
        if(isVIP == true) {
            discount = 5.0;
        }
        else {
            discount = 0.0;
        }
        
        return discount;
    }

    public String getName() {
        return name;
    }
}