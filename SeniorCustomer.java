public class SeniorCustomer extends Customer {
    public SeniorCustomer(String name, String ic, boolean isVIP) {
        super(name, ic, isVIP);
    }

    public double getDiscount() {
        return 5.00;
    }
    
    public String getCategory() {
        return "Senior Citizen";
    }
}