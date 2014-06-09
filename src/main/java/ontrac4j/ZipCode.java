package ontrac4j;

public class ZipCode {
    private final ontrac4j.xml.ZipCode zipCode;

    public ZipCode(ontrac4j.xml.ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode.getZipCode();
    }

    public boolean isSaturdayServiced() {
        return zipCode.getSaturdayServiced() == 1;
    }

    public boolean isPickupServiced() {
        return zipCode.getPickupServiced() == 1;
    }

    public boolean isPalletizedServiced() {
        return zipCode.getPalletizedServiced() == 1;
    }

    public String getSortCode() {
        return zipCode.getSortCode();
    }
}
