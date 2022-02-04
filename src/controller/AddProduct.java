package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that allows you to add a Product.
 *
 * @author Aaron
 * */

public class AddProduct implements Initializable {


    private ObservableList<Part> partsAddedToProd = FXCollections.observableArrayList();
    public TextField searchForParts;
    public Button addProd;
    public Button addProdExit;
    public TextField addProdId;
    public TextField addProdName;
    public TextField addProdInv;
    public TextField addProdMax;
    public TextField addProdMin;
    public TextField addProdPrice;
    public TableView<Part> addPartTable;
    public TableColumn<Part, Integer> addPartIdCol;
    public TableColumn<Part, String> addPartNameCol;
    public TableColumn<Part, Integer> addPartInvCol;
    public TableColumn<Part, Double> addPartPriceCol;
    public TableView<Part> rmvPartTable;
    public TableColumn<Part, Integer> rmvPartIdCol;
    public TableColumn<Part, String> rmvPartNameCol;
    public TableColumn<Part, Integer> rmvPartInvCol;
    public TableColumn<Part, Double> rmvPartPriceCol;
    public Button addPartToProd;
    public Button removePartFromProd;

    /**
     *verifies that user inputted min is less than user inputted max.
     * @param min user inputted minimum
     * @param max user inputted maximum
     * @return boolean that tells us if it validates
     * */
    boolean minAndMaxWork(int min, int max) {
        boolean works = true;
        if (min < 1 || min >= max) {
            works = false;
            listOfErrors(3);
        }
        return works;
    }
    /**
     *verifies that user inputted inventory is between max and min numbers
     * @param max user inputted max
     * @param min user inputted min
     * @param inv user inputted inv
     * @return boolean that tells us if it validates
     * */
    boolean invWorks(int inv, int min, int max) {
        boolean works = true;
        if (inv > max || inv < min) {
            works = false;
            listOfErrors(2);
        }

        return works;
    }
    /**
     * Adds a new product to inventory, then loads the main screen.
     * Has error pop-ups to prevent blank fields and verify stock is possible.
     * @param actionEvent Add product button event.
     * */
    public void onAddProd(ActionEvent actionEvent) throws IOException {
        try {
            int prodId = 0;
            String prodName = addProdName.getText();
            Double price = Double.parseDouble(addProdPrice.getText());
            int inv = Integer.parseInt(addProdInv.getText());
            int min = Integer.parseInt(addProdMin.getText());
            int max = Integer.parseInt(addProdMax.getText());

            if (prodName.isEmpty()) {
                listOfErrors(1);
            } else {
                if (minAndMaxWork(min,max) && invWorks(inv, min, max)) {
                    Product newProd = new Product(prodId, prodName, price, inv, min, max);
                    for (Part part : partsAddedToProd) {
                        newProd.addPartsOfTheProd(part);
                    }
                    newProd.setId(Inventory.createNewProdId());
                    Inventory.addProd(newProd);
                    goBackToMain(actionEvent);
                }
            }
        } catch (Exception e) {
            listOfErrors(4);
        }

    }
    /**
     * goes back to the main screen.
     * an alert occurs asking the user to confirm.
     * @param actionEvent press exit button
     * */
    public void onAddProdExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Would you like to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            goBackToMain(actionEvent);
        }
    }
    /**
     * Loads the Main Screen
     * @param actionEvent comes from parent method
     * */
    private void goBackToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Add parts to associated parts list and populates the associated parts table.
     * an alert occurs if no part is selected.
     * @param actionEvent clicking the add part button
     * */
    public void onAddPartToProd(ActionEvent actionEvent) {
        Part thisPart = addPartTable.getSelectionModel().getSelectedItem();

        if (thisPart == null) {
            listOfErrors(6);
        } else {
            partsAddedToProd.add(thisPart);
            rmvPartTable.setItems(partsAddedToProd);
        }
    }
    /**
     *Removes part from being associated with the product and removes from the corresponding table.
     * An error occurs is no part is selected
     * @param actionEvent clicking the remove part button
     * */
    public void onRemovePartFromProd(ActionEvent actionEvent) {
        Part thisPart = rmvPartTable.getSelectionModel().getSelectedItem();

        if (thisPart == null) {
            listOfErrors(6);
        } else {
            Alert confirmRemoval = new Alert(Alert.AlertType.CONFIRMATION);
            confirmRemoval.setTitle("Alert");
            confirmRemoval.setContentText("Remove Selected Part?");
            Optional<ButtonType> answer = confirmRemoval.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                partsAddedToProd.remove(thisPart);
                rmvPartTable.setItems(partsAddedToProd);
            }

        }
    }
    /**
     *Searches for parts by part ID or by Part Name.
     *an alert occurs if no results are found.
     * @param actionEvent typing enter after entering text in the search field.
     * */
    public void onSearchForParts(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsSearched = FXCollections.observableArrayList();
        String searched = searchForParts.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searched) || part.getName().contains(searched)) {
                partsSearched.add(part);
            }
        }

        addPartTable.setItems(partsSearched);
        //set an error here later for zero items found.
        if (partsSearched.isEmpty()) {
            listOfErrors(5);
        }
    }
    /**
     *sets the parts table to display all parts when the search field is empty.
     * @param keyEvent every key press
     * */
    public void onSearchForPartsKeyPress(KeyEvent keyEvent) {
        String searchText = searchForParts.getText();
        if (searchText.isEmpty()) {
            addPartTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     *Initializes Add Product controller and populates the all parts table.
     * @param resourceBundle
     * @param url
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addPartTable.setItems(Inventory.getAllParts());

        rmvPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        rmvPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rmvPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        rmvPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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
                alert.setContentText("Please enter a Product name");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Your Inventory must be a number equal to or between your Minimum and Maximum");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Your minimum must be less than your maximum");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please fill out all fields");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("No Parts Found");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please select a part.");
                alert.showAndWait();
                break;

        }

    }


}
