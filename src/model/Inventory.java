package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Model for all Inventory
 *
 * @author Aaron
 * */

public class Inventory {
    private static int partId = 0;
    private static int prodId = 0;



    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * @return all parts
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /**
     * @return all products
     * */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    /**
     * @param newPart adds to allParts
     * */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    /**
     * @param newProd adds to allProducts
     * */
    public static void addProd(Product newProd) {
        allProducts.add(newProd);
    }

    /**
     * @return an incremented partId
     * */
    public static int createNewPartId() {
        return ++partId;
    }

    /**
     * @return an incremented prodId
     * */
    public static int createNewProdId() {
        return ++prodId;
    }
    /**
     * Deletes part from allParts
     * @param thisPart deletes thisPart
     * @return boolean if removed.
     * */
    public static <Part> boolean deletePart(Part thisPart) {
        if (allParts.contains(thisPart)) {
            allParts.remove(thisPart);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes part from allParts
     * @param selectedProduct deletes selectedProduct
     * @return boolean if removed.
     * */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
}
