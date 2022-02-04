package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Part;
import model.Inventory;
import model.Outsourced;
import model.InHouse;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class that allows you to modify a selected part.
 *
 * @author Aaron
 *
 * */

public class ModifyPart implements Initializable {

    public Button modPartSave;
    public Button modPartExit;
    public TextField modPartId;
    public TextField modPartName;
    public TextField modPartInv;
    public TextField modPartPrice;
    public TextField modPartMax;
    public TextField modPartMin;
    public TextField modMachineId;
    public Label modMachineIdLabel;
    public RadioButton inHouseTgl;
    public ToggleGroup partType;
    public RadioButton outsourcedTgl;

    private Part moddedPart;

    /**
     *Sets Machine ID label text to Machine ID
     * @param actionEvent clicking the InHouse toggle
     * */
    public void onInHouseTgl(ActionEvent actionEvent) {
        modMachineIdLabel.setText("Machine ID");
    }
    /**
     *Sets Machine ID label text to Co. Name
     * @param actionEvent clicking the Outsourced toggle
     * */
    public void onOutsourcedTgl(ActionEvent actionEvent) {
        modMachineIdLabel.setText("Co. Name");
    }
    /**
     *Deletes the loaded part and creates a new part with the existing ID and details provided by the user.
     * Verifies that all fields are entered with valid data and presents error to the user if something is not valid.
     * @param actionEvent clicking the save button
     * */
    public void onModPartSave(ActionEvent actionEvent) {
        try {
            int partId = moddedPart.getId();
            String partName = modPartName.getText();
            Double partPrice = Double.parseDouble(modPartPrice.getText());
            int partInv = Integer.parseInt(modPartInv.getText());
            int partMin = Integer.parseInt(modPartMin.getText());
            int partMax = Integer.parseInt(modPartMax.getText());
            int machineId;
            String coName;
            boolean partAdded = false;

            if (partName.isEmpty()) {
                listOfErrors(1);
            } else if (minWorks(partMin, partMax) && invWorks(partMin, partMax, partInv)) {
                if (inHouseTgl.isSelected()) {
                    try {
                        machineId = Integer.parseInt(modMachineId.getText());
                        InHouse newInHouse = new InHouse(partId, partName, partPrice, partInv, partMin, partMax, machineId);
                        Inventory.addPart(newInHouse);
                        partAdded = true;
                    } catch (Exception e) {
                        listOfErrors(2);
                    }
                }
                if (outsourcedTgl.isSelected()) {
                    coName = modMachineId.getText();
                    Outsourced newOutsourced = new Outsourced(partId, partName, partPrice, partInv, partMin, partMax, coName);
                    Inventory.addPart(newOutsourced);
                    partAdded = true;
                }

                if (partAdded) {
                    Inventory.deletePart(moddedPart);
                    goBackToMain(actionEvent);
                }
            }
        }
        catch (Exception e) {
            listOfErrors(5);
        }
    }
    /**
     *Exits to the main screen without saving.
     * An alert pops up to the user to confirm.
     * @param actionEvent clicking the exit button.
     * */
    public void onModPartExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Would you like to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            goBackToMain(actionEvent);
        }
    }
    /**
     *Goes back to the main screen.
     * @param actionEvent passed from a parent function.
     * */
    private void goBackToMain(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     *Verifies that the minimum is less than the maximum
     * @param max user inputted max
     * @param min user inputted min
     * @return works if everything validates.
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
     *verifies that inventory number is in between the minimum and maximum
     * @param min user inputted min
     * @param max user inputted max
     * @param inv user inputted inv
     * @return true if everything validates.
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
     *Initializes the controller
     * Loads the selected part and places the data in a table.
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        moddedPart = MainScreen.getModThisPart();

        if (moddedPart instanceof InHouse) {
            inHouseTgl.setSelected(true);
            modMachineIdLabel.setText("Machine ID");
            modMachineId.setText(String.valueOf(((InHouse) moddedPart).getMachineId()));
        }

        if (moddedPart instanceof Outsourced) {
            outsourcedTgl.setSelected(true);
            modMachineIdLabel.setText("Co. Name");
            modMachineId.setText(String.valueOf(((Outsourced) moddedPart).getCoName()));
        }

        modPartId.setText(String.valueOf(moddedPart.getId()));
        modPartName.setText(String.valueOf(moddedPart.getName()));
        modPartPrice.setText(String.valueOf(moddedPart.getPrice()));
        modPartInv.setText(String.valueOf(moddedPart.getStock()));
        modPartMax.setText(String.valueOf(moddedPart.getMax()));
        modPartMin.setText(String.valueOf(moddedPart.getMin()));
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
        }
    }
}
