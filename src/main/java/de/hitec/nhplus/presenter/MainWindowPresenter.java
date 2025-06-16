package de.hitec.nhplus.presenter;

import de.hitec.nhplus.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * The <code>MainWindowPresenter</code> contains the entire logic of the mainWindow view. It determines which data is displayed and how to react to events.
 */
public class MainWindowPresenter {

    @FXML
    private BorderPane mainBorderPane;

    /**
     * Handles the event to display the view showing all patients. This method replaces the center of the mainBorderPane
     * with the content loaded from the "AllPatientView.fxml" file.
     *
     * @param event the action event triggered, typically by a user interaction such as clicking a button
     */
    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the event triggered to display all treatments in the main window.
     * It loads the AllTreatmentView and sets it as the center content of the main border pane.
     *
     * @param event the event that triggered this method, typically an action event from a button press or menu selection
     */
    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the event of showing the "All Caregivers" view.
     * This method loads the view from the corresponding FXML file and sets it
     * as the content of the main border pane in the application window.
     *
     * @param event the ActionEvent triggered by user interaction, typically a button click
     */
    @FXML
    private void handleShowAllCaregivers(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllCaregiverView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
