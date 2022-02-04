package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/***
 * The Main screen controller. This directs you to the screens you need to see, and delete products and parts from the inventory.
 *
 *
 * @author Aaron
 */


public class MainScreen implements Initializable {
    @FXML
    private TextField partSearchBar;
    @FXML
    private TableColumn partIdCol;
    @FXML
    private TableColumn partNameCol;
    @FXML
    private TableColumn partInStockCol;
    @FXML
    private TableColumn partCostPerUnitCol;
    @FXML
    private Button addPart;
    @FXML
    private Button modifyPart;
    @FXML
    private Button deletePart;
    @FXML
    private TextField productSearchBar;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInStockCol;
    @FXML
    private TableColumn<Product, Double> productCostPerUnitCol;
    @FXML
    private Button addProduct;
    @FXML
    private Button modifyProduct;
    @FXML
    private Button exitButton;
    @FXML
    private Button deleteProduct;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;

    @FXML
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    @FXML
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();


    private static Part modThisPart;
    public static Product modThisProd;

    /**
     * Method to return a selected part
     * @return the selected part
     * */
    public static Part getModThisPart() {
        return modThisPart;
    }
    /**
     * Method to return a selected product
     * @return the selected product
     * */
    public static Product getModThisProd() {
        return modThisProd;
    }
    /**
     * Initialized the MainScreen controller.
     * Populates tables with inventory of parts and products.
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems((ObservableList<Part>) Inventory.getAllParts());
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));


        productsTable.setItems((ObservableList<Product>) Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productInStockCol.setCellValueFactory(new PropertyValueFactory<>("inv"));

    }
    /**
     *sets the parts table to display all parts when the search field is empty.
     * @param keyEvent every key press
     * */
    @FXML
    public void onSearchKeyPressed(KeyEvent keyEvent) {
        String searchText = partSearchBar.getText();
        if (searchText.isEmpty()) {
            partsTable.setItems(Inventory.getAllParts());
        }
    }
    /**
     *Searches for parts by part ID or by Part Name.
     *an alert occurs if no results are found.
     * @param actionEvent typing enter after entering text in the search field.
     * */
    @FXML
    public void onPartSearch(ActionEvent actionEvent) {
        String textSearched = partSearchBar.getText();

        allParts = Inventory.getAllParts();
        ObservableList<Part> searchedParts = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(textSearched) || part.getName().contains(textSearched) ) {
                searchedParts.add(part);
            }
        }

        partsTable.setItems(searchedParts);
        if (searchedParts.isEmpty()) {
            listOfErrors(3);
        }

    }

    /**
     * Loads the AddPart screen.
     * @param actionEvent clicking the add part button
     * */
    @FXML
    public void onAddPart(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Loads the ModifyPart screen.
     * An alert occurs if a part is not selected.
     * @param actionEvent clicking the modify part button
     * */
    @FXML
    public void onModifyPart(ActionEvent actionEvent) throws IOException {

        modThisPart = partsTable.getSelectionModel().getSelectedItem();
        if (modThisPart == null) {
            listOfErrors(1);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

/**
 * Deletes the selected Part.
 * If a part is not selected, and alert occurs.
 * Requires confirmation by user to delete selected item.
 * @param actionEvent clicking the delete part button
 * */
    @FXML
    void onDeletePart(ActionEvent actionEvent) {
        Part thisPart = partsTable.getSelectionModel().getSelectedItem();
        if (thisPart == null) {
            listOfErrors(1);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Would you like to delete this part?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                Inventory.deletePart(thisPart);
            }
        }
    }

    /**
     *Searches for parts by product ID or by product name.
     *an alert occurs if no results are found.
     * @param actionEvent typing enter after entering text in the search field.
     * */
    public void onProductSearch(ActionEvent actionEvent) {
        System.out.println("prod searched");

        String textSearched = productSearchBar.getText();

        allProducts = Inventory.getAllProducts();
        ObservableList<Product> searchedProd = FXCollections.observableArrayList();

        for (Product prod : allProducts) {
            if (String.valueOf(prod.getId()).contains(textSearched) || prod.getName().contains(textSearched) ) {
                searchedProd.add(prod);
            }
        }

        productsTable.setItems(searchedProd);
        if (searchedProd.isEmpty()) {
            listOfErrors(4);
        }
    }
    /**
     *sets the parts table to display all parts when the search field is empty.
     * @param keyEvent every key press
     * */
    public void onProdSearchKeyPressed(KeyEvent keyEvent) {
        String searchText = productSearchBar.getText();
        if (searchText.isEmpty()) {
            productsTable.setItems(Inventory.getAllProducts());
        }

    }

    /**
     * Loads the AddProduct screen.
     * @param actionEvent clicking the add product button.
     * */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the ModifyProduct screen.
     * Stores the selected product in a variable to be accessed later.
     * If no product is selected, an alert occurs.
     * @param actionEvent clicking the modify product button.
     * */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {

        modThisProd = productsTable.getSelectionModel().getSelectedItem();

        if (modThisProd == null) {
            listOfErrors(2);
        } else {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyProduct.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes the selected Product.
     * If a product is not selected, an alert occurs.
     * Requires confirmation by user to delete selected item.
     * If there are parts still associated with the product, an error occurs.
     * @param actionEvent clicking the delete product button
     * */
    @FXML
    void onDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            listOfErrors(2);
        } else {
            if (selectedProduct.getAllPartsOfTheProd().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Alert");
                alert.setContentText("Would you like to delete this product?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct((model.Product) selectedProduct);
                }
            } else {
                listOfErrors(5);
            }
        }
        System.out.println("del prod clicked");
    }

/**
 * Exits the Program.
 * @param actionEvent clicking the exit button.
 * */
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
        System.out.println("exit clicked");
    }

    /**
     *A list of errors to run in different cases
     * @param whichError tells the method which error to display
     * */
    private void listOfErrors(int whichError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch(whichError) {
            case 1:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please Select a Part");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please Select a Product");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("No Parts Found");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("No Products Found");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("You must remove all parts from this product before you can delete it.");
                alert.showAndWait();
                break;

        }

    }



}
