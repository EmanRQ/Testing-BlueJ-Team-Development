public class StudentCustomer extends Customer {
    public StudentCustomer(String name, String ic, boolean isVIP) {
        super(name, ic, isVIP);
    }

    public double getDiscount() {
        return 2.00;
    }
    
    public String getCategory() {
        return "Student";
    }
}