package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that allows you to add a part.
 *
 * @author
 *
 * */

public class AddPart implements Initializable  {


    public Button newPartSave;
    public Button newPartExit;
    public TextField newPartId;
    public TextField newPartName;
    public TextField newPartInv;
    public TextField newPartPrice;
    public TextField newPartMax;
    public TextField newPartMin;
    public TextField newMachineId;
    public RadioButton inHouseTgl;
    public RadioButton outsourcedTgl;
    public ToggleGroup partType;
    public Label machineLabel;

    /**
     * Adds a new part to inventory, then loads the main screen.
     * Has error pop-ups to prevent blank fields and verify stock is possible.
     * @param actionEvent Add part button event.
     * */
    @FXML
    void onNewPartSave(ActionEvent actionEvent) throws IOException {
        try {
            int partId = 0;
            String partName = newPartName.getText();
            Double partPrice = Double.parseDouble(newPartPrice.getText());
            int partInv = Integer.parseInt(newPartInv.getText());
            int partMin = Integer.parseInt(newPartMin.getText());
            int partMax = Integer.parseInt(newPartMax.getText());
            int machineID;
            String coName;
            boolean partAdded = false;

            if (!inHouseTgl.isSelected() && !outsourcedTgl.isSelected()) {
                listOfErrors(6);
            }

            if (partName.isEmpty()) {
                listOfErrors(1);
            } else {
                if (minWorks(partMin, partMax) && invWorks(partMin, partMax, partInv)) {
                    if (inHouseTgl.isSelected()) {
                        try {
                            machineID = Integer.parseInt(newMachineId.getText());
                            InHouse newInHouse = new InHouse(partId, partName, partPrice, partInv, partMin, partMax, machineID);
                            newInHouse.setId(Inventory.createNewPartId());
                            Inventory.addPart(newInHouse);
                            partAdded = true;
                        } catch (Exception e) {
                            listOfErrors(2);
                        }
                    }

                    if (outsourcedTgl.isSelected()) {
                        coName = newMachineId.getText();
                        Outsourced newOutsourced = new Outsourced(partId, partName, partPrice, partInv, partMin, partMax, coName);
                        Inventory.addPart(newOutsourced);
                        newOutsourced.setId(Inventory.createNewPartId());
                        partAdded = true;

                    }

                    if (partAdded) {goBackToMain(actionEvent);}
                }
            }

        } catch (Exception e){
            listOfErrors(5);
        }
    }
    /**
     *Verifies that the minimum number is less than the maximum number
     * @param max to check
     * @param min to check
     * @return works boolean to verify
     * */
    private boolean minWorks(int min, int max) {
        boolean works = true;
        if (0 > min || max <= min) {
            works = false;
            listOfErrors(4);
        }
        return works;
    }

    /**
     *Verifies that the inventory number is inbetween the minimum and maximum stock numbers.
     * @param min minimum stock
     * @param max maximum stock
     * @param inv current inventory
     * @return works boolean to verify
     * */
    private boolean invWorks(int min, int max, int inv) {
        boolean works = true;
        if (min > inv || max < inv) {
            works = false;
            listOfErrors(3);
        }
        return works;
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
     * goes back to the main screen
     * @param actionEvent press exit button
     * */
    public void onNewPartExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Would you like to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            goBackToMain(actionEvent);
        }
    }

    /**
     *Initializes Controller
     * @param resourceBundle
     * @param url
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                alert.setContentText("Please enter a part name");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("The machine ID must only contain numbers");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Your Inventory must be a number equal to or between your Minimum and Maximum");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Your minimum must be less than your maximum");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please fill out all fields");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error");;
                alert.setHeaderText("Error");
                alert.setContentText("Please select In-House or Outsourced");
                alert.showAndWait();
                break;

        }

    }
    /**
     *Sets machineLabel text to Machine ID
     * @param actionEvent clicking the InHouse Toggle.
     * */
    public void onInHouseTgl(ActionEvent actionEvent) {
        machineLabel.setText("Machine ID");

    }
    /**
     *Sets machineLabel text to Co. Name
     * @param actionEvent clicking on the outsourced toggle
     * */
    public void onOutsourcedTgl(ActionEvent actionEvent) {
        machineLabel.setText("Co. Name");
    }
}
