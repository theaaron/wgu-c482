package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * The model for a product that can also contain parts to be associated with.
 *
 * @author Aaron
 * */

public class Product {
    private int id;
    private String name;
    private double price;
    private int inv;
    private int min;
    private int max;

    public ObservableList<Part> partsOfTheProd = FXCollections.observableArrayList();
    /**
     * The constructor. Creates a new instance of a product.
     * @param id product id
     * @param name product name
     * @param price product price
     * @param inv product inventory
     * @param min product minimum inventory
     * @param max product maximum inventory
     * */
    public Product(int id, String name, double price, int inv, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.min = min;
        this.max = max;
    }

    //setters and getters
    /**@return the ID
     * */
    public int getId() {
        return id;
    }
    /**
     * @param id sets as id
     * */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name
     * */
    public String getName() {
        return name;
    }
    /**
     * @param name sets as name
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the price
     * */
    public double getPrice() {
        return price;
    }
    /**
     * @param price sets as price
     * */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * @return the inventory
     * */
    public int getInv() {
        return inv;
    }
    /**
     * @param inv sets as inventory
     * */
    public void setInv(int inv) {
        this.inv = inv;
    }
    /**
     * @return the minimum
     * */
    public int getMin() {
        return min;
    }
    /**
     * @param min sets as minimum
     * */
    public void setMin(int min) {
        this.min = min;
    }
    /**
     * @return the maximum
     * */
    public int getMax() {
        return max;
    }
    /**
     * @param max sets as maximum
     * */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param part adds to list of parts associated with product
     * */
    public void addPartsOfTheProd(Part part) {
        partsOfTheProd.add(part);
    }
    /**
     * @param part removes from the list of parts associated with the product
     * */
    public void removePartsOfTheProd(Part part) {
        partsOfTheProd.remove(part);
    }
    /**
     * @return all parts associated with the product.
     * */
    public ObservableList<Part> getAllPartsOfTheProd() {return partsOfTheProd;}

















}
