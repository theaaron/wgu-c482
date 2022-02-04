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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class that allows you to modify a selected product.
 *
 * @author Aaron
 * */

public class ModifyProduct implements Initializable {

    private ObservableList<Part> partsAddedToProd = FXCollections.observableArrayList();
    @FXML
    public Button modProd;
    @FXML
    public Button modProdExit;
    @FXML
    public TextField modProdId;
    @FXML
    public TextField modProdName;
    @FXML
    public TextField modProdInv;
    @FXML
    public TextField modProdMax;
    @FXML
    public TextField modProdMin;
    @FXML
    public TextField modProdPrice;
    @FXML
    public TableView addPartTable;
    @FXML
    public TableColumn addPartIdCol;
    @FXML
    public TableColumn addPartNameCol;
    @FXML
    public TableColumn addPartInvCol;
    @FXML
    public TableColumn addPartPriceCol;
    @FXML
    public TableView rmvPartTable;
    @FXML
    public TableColumn rmvPartIdCol;
    @FXML
    public TableColumn rmvPartNameCol;
    @FXML
    public TableColumn rmvPartInvCol;
    @FXML
    public TableColumn rmvPartPriceCol;
    @FXML
    public Button addPartToProd;
    @FXML
    public Button removePartFromProd;
    @FXML
    public TextField searchForParts;

    private Product prodToMod;
    private ObservableList<Part> partsToMod = FXCollections.observableArrayList();

    /**
     *Deletes the loaded product and creates a new product with the existing ID and details provided by the user.
     * Verifies that all fields are entered with valid data and presents error to the user if something is not valid.
     * @param actionEvent clicking the save button
     * */
    @FXML
    public void onModProd(ActionEvent actionEvent) throws IOException {
        try {
            int prodId = prodToMod.getId();
            String prodName = modProdName.getText();
            Double price = Double.parseDouble(modProdPrice.getText());
            int inv = Integer.parseInt(modProdInv.getText());
            int min = Integer.parseInt(modProdMin.getText());
            int max = Integer.parseInt(modProdMax.getText());

            if (prodName.isEmpty()) {
                listOfErrors(1);
            } else {
                if (minAndMaxWork(min,max) && invWorks(inv, min, max)) {
                    Product newProd = new Product(prodId, prodName, price, inv, min, max);
                    for (Part part : partsAddedToProd) {
                        newProd.addPartsOfTheProd(part);
                    }

                    Inventory.addProd(newProd);
                    Inventory.deleteProduct(prodToMod);
                    System.out.println(newProd);
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
    public void onModProdExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Would you like to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            goBackToMain(actionEvent);
        }

    }
    /**
     * Add parts to associated parts list and populates the associated parts table.
     * an alert occurs if no part is selected.
     * @param actionEvent clicking the add part button
     * */
    @FXML
    public void onAddPartToProd(ActionEvent actionEvent) {
        Part thisPart = (Part) addPartTable.getSelectionModel().getSelectedItem();

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
    @FXML
    public void onRemovePartFromProd(ActionEvent actionEvent) {
        Part thisPart = (Part) rmvPartTable.getSelectionModel().getSelectedItem();

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
     *Initializes ModifyProduct controller.
     * Populates the all parts table and the associated parts tables and text fields with data from selected product.
     * @param resourceBundle
     * @param url
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prodToMod = MainScreen.getModThisProd();
        partsToMod = prodToMod.getAllPartsOfTheProd();

        addPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addPartTable.setItems(Inventory.getAllParts());

        rmvPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        rmvPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        rmvPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        rmvPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        rmvPartTable.setItems(partsToMod);

        modProdName.setText(prodToMod.getName());
        modProdPrice.setText(String.valueOf(prodToMod.getPrice()));
        modProdInv.setText(String.valueOf(prodToMod.getInv()));
        modProdMin.setText(String.valueOf(prodToMod.getMin()));
        modProdMax.setText(String.valueOf(prodToMod.getMax()));
    }
    /**
     *Searches for parts by part ID or by Part Name.
     *an alert occurs if no results are found.
     * @param actionEvent typing enter after entering text in the search field.
     * */
    @FXML
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
        if (partsSearched.isEmpty()) {listOfErrors(5);}

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
