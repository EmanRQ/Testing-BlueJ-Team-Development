public class NormalCustomer extends Customer {
    public NormalCustomer(String name, String ic, boolean isVIP) {
        super(name, ic, isVIP);
    }

    public double getDiscount() {
        return 0.00;
    }
    
    public String getCategory() {
        return "Normal";
    }
}