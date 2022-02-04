package model;

/**
 * Model for an Outsourced Part
 *
 * @author Aaron
 *
 * */
public class Outsourced extends Part {
    private String coName;

    /**
     * Constructs an Outsourced Part
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min part min
     * @param max part max
     * @param coName part Company name
     * */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String coName) {
        super(id, name, price, stock, min, max);
        this.coName = coName;
    }
    /**
     * @return company name
     * */
    public String getCoName() {
        return this.coName;
    }
    /**
     * @param coName sets company name
     * */
    public void setCoName(String coName) {
        this.coName = coName;
    }
}
