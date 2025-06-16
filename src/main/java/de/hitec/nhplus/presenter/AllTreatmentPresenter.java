package de.hitec.nhplus.presenter;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Caregiver;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Person;
import de.hitec.nhplus.model.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * The AllTreatmentPresenter class is responsible for managing the user interface and data
 * interactions associated with displaying and handling treatments in a healthcare application.
 * It interacts with the view components such as TableView, ComboBoxes, and Buttons, as well
 * as the underlying data layer (via DAOs), to enable functionalities such as displaying,
 * filtering, adding, and deleting treatments.
 */

public class AllTreatmentPresenter {

    @FXML
    private TableView<Treatment> tableView;

    @FXML
    private TableColumn<Treatment, Integer> columnId;

    @FXML
    private TableColumn<Treatment, Integer> columnPid;

    @FXML
    private TableColumn<Treatment, String> columnDate;

    @FXML
    private TableColumn<Treatment, String> columnBegin;

    @FXML
    private TableColumn<Treatment, String> columnEnd;

    @FXML
    private TableColumn<Treatment, String> columnDescription;

    @FXML
    private ComboBox<String> comboBoxPatientSelection;

    @FXML
    private ComboBox<String> comboBoxCaregiverSelection;

    @FXML
    private Button buttonNewTreatment;

    @FXML
    private Button buttonDelete;

    private TreatmentDao dao;
    private final ObservableList<String> patientSelection = FXCollections.observableArrayList();
    private final ObservableList<String> caregiverSelection = FXCollections.observableArrayList();
    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();
    private ArrayList<Patient> patientList;
    private ArrayList<Caregiver> caregiverList;
    private final String ALL_PATIENTS = "alle Patienten";
    private final String ALL_CAREGIVERS = "alle Pflegekräfte";


    /**
     * Initializes the view and sets up necessary configurations for displaying and managing treatments.
     * This method performs the following actions:
     * - Populates data into TableView and ComboBoxes.
     * - Configures columns in the TableView for displaying treatment attributes.
     * - Assigns data to the ComboBoxes for selecting patients and caregivers, and sets default selections.
     * - Disables the delete button until a treatment is selected in the TableView.
     * - Adds a listener to manage the delete button's state based on TableView selection changes.
     * - Initializes DAOs for retrieving necessary data from the database.
     * - Prepares the patient and caregiver lists for use in ComboBoxes.
     *
     * Throws a RuntimeException if a SQL exception is encountered during DAO retrieval or data loading.
     */
    public void initialize() {
        readAllAndShowInTableView();
        comboBoxPatientSelection.setItems(patientSelection);
        comboBoxPatientSelection.getSelectionModel().select(0);

        comboBoxCaregiverSelection.setItems(caregiverSelection);
        comboBoxCaregiverSelection.getSelectionModel().select(0);

        this.columnId.setCellValueFactory(new PropertyValueFactory<>("tid"));
        this.columnPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.columnBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.columnEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.tableView.setItems(this.treatments);

        // Disabling the button to delete treatments as long, as no treatment was selected.
        this.buttonDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldTreatment, newTreatment) ->
                        AllTreatmentPresenter.this.buttonDelete.setDisable(newTreatment == null));

        try {
            dao = DaoFactory.getDaoFactory().createTreatmentDao();
            patientList = (ArrayList<Patient>) DaoFactory.getDaoFactory().createPatientDao().readAll();
            caregiverList = (ArrayList<Caregiver>) DaoFactory.getDaoFactory().createCaregiverDao().readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.createComboBoxDataForPatient();
        this.createComboBoxDataForCaregiver();

    }

    /**
     * Reads all treatment records from the database and displays them in the
     * associated TableView component. The method also ensures that default
     * selections are set for patient and caregiver ComboBox components.
     *
     * The DAO is initialized using the {@code DaoFactory}, and the treatments
     * retrieved from the database are added to the list backing the TableView.
     * If an SQL exception occurs during the process, it is caught and printed
     * to the standard error output.
     */
    public void readAllAndShowInTableView() {
        comboBoxPatientSelection.getSelectionModel().select(0);
        comboBoxCaregiverSelection.getSelectionModel().select(0);
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            this.treatments.clear();
            this.treatments.addAll(dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Populates a ComboBox with values based on a list of Person objects.
     * Clears the current ObservableList, adds a defined first option, formats
     * and adds display names of Person objects, and sets the ComboBox items accordingly.
     *
     * @param <T> The type of Person, must extend the Person class.
     * @param selectionList The ObservableList to store and manage the ComboBox items.
     * @param comboBox The ComboBox to be populated with selection options.
     * @param personList The List of Person objects from which the selection options are formed.
     * @param firstOption The predefined first option to be added and selected by default.
     */
    public <T extends Person> void createComboBoxData(ObservableList<String> selectionList, ComboBox<String> comboBox, List<T> personList, String firstOption) {
        selectionList.clear();
        selectionList.add(firstOption);
        for (Person person : personList) {
            selectionList.add(formatPersonDisplayName(person));
        }
        comboBox.setItems(selectionList);
        comboBox.getSelectionModel().selectFirst(); // "alle ..." wird vorausgewählt
    }


    /**
     * Populates the combo box for patient selection with relevant data.
     *
     * This method initializes the patient selection combo box with a list of options,
     * including a default option (e.g., "all patients") followed by the formatted display
     * names of all patients available in the patient list. It achieves this by calling
     * the generic {@code createComboBoxData} method, which handles the setup of the
     * selection list and binding it to the combo box.
     *
     * This method ensures the patient selection combo box is always populated with
     * up-to-date and correctly formatted data.
     */
    private void createComboBoxDataForPatient() {
        createComboBoxData(patientSelection, comboBoxPatientSelection, patientList, ALL_PATIENTS);
    }

    /**
     * Populates the caregiver selection ComboBox with data.
     *
     * This method creates and populates the caregiver selection ComboBox with a list
     * of caregivers, including an initial "All Caregivers" option. The method utilizes
     * the reusable `createComboBoxData` method to accomplish the following:
     */
    private void createComboBoxDataForCaregiver() {
        createComboBoxData(caregiverSelection, comboBoxCaregiverSelection, caregiverList, ALL_CAREGIVERS);
    }

    /**
     * Handles the filtering and synchronization of treatments based on the selected
     * values in the patient and caregiver combo boxes. This method updates the internal
     * treatments list to include only those treatments that are associated with the currently
     * selected patient and caregiver.
     */
    @FXML
    public void handleComboBoxes() {
        this.treatments.clear();
        List<Treatment> patientTreatments = getTreatments(comboBoxPatientSelection, "pid", patientList);
        List<Treatment> caregiverTreatments = getTreatments(comboBoxCaregiverSelection, "cgid", caregiverList);

        for (Treatment treatment : patientTreatments) {
            for (Treatment treatmentCaregiver : caregiverTreatments) {
                if (treatmentCaregiver.getTid() == treatment.getTid()) {
                    this.treatments.add(treatment);
                }
            }
        }
    }

    /**
     * Retrieves a list of treatments based on the provided selection and person information.
     *
     * @param <T> The type parameter indicating the type of the {@link Person}.
     * @param comboBoxSelection The ComboBox containing the current selection used to filter treatments.
     * @param personId The unique identifier of the person related to the treatments.
     * @param personList A list of {@code Person} objects to search for the selected person.
     * @return A list of {@link Treatment} objects corresponding to the selection and person information.
     *         Returns all treatments if the selection encompasses all patients or caregivers.
     * @throws RuntimeException If an SQL exception occurs while querying the treatments.
     */
    public <T extends Person> List<Treatment> getTreatments(ComboBox<String> comboBoxSelection, String personId, List<T> personList) {
        String selectedPerson = comboBoxSelection.getSelectionModel().getSelectedItem();
        List<Treatment> personTreatment = new ArrayList<>();
        try {
            if (selectedPerson == null || selectedPerson.equals(ALL_PATIENTS) || selectedPerson.equals(ALL_CAREGIVERS)) {
                personTreatment.addAll(this.dao.readAll());
            } else {
                Person person = getPersonFromDisplayName(selectedPerson, personList);
                if (person != null) {
                    personTreatment.addAll(this.dao.readTreatmentsById(person.getId(), personId));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return personTreatment;
    }

    /**
     * Formats the display name of a given person by combining their surname and first name.
     * The resulting string is structured as "surname, firstName".
     *
     * @param person the Person object whose display name is to be formatted
     * @return a string representing the person's display name in the format "surname, firstName"
     */
    private String formatPersonDisplayName(Person person) {
        return String.format("%s, %s", person.getSurname(), person.getFirstName());
    }

    /**
     * Retrieves a person from the provided list based on the display name.
     * The display name is compared with the formatted display name derived
     * from each person in the list.
     *
     * @param displayName The formatted display name to match against.
     * @param personList The list of persons to search within.
     * @param <T> The type of person, which must extend the {@link Person} class.
     * @return The person matching the display name, or null if no match is found.
     */
    private <T extends Person> T getPersonFromDisplayName(String displayName, List<T> personList) {
        return personList.stream().filter(person -> displayName.equals(formatPersonDisplayName(person))).findFirst().orElse(null);
    }

    /**
     * Handles the deletion of a selected treatment from the table view and database.
     *
     * The method retrieves the currently selected treatment from the table view and removes it from the
     * internal `treatments` list. It then uses the `TreatmentDao` to delete the corresponding treatment
     * record from the database. If an SQL exception occurs during the operation, it is caught and the
     * stack trace is printed.
     *
     * This method assumes that a valid treatment entry is selected in the table view before invocation.
     * Failure to select an entry will result in an exception due to invalid index access.
     */
    @FXML
    public void handleDelete() {
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment t = this.treatments.remove(index);
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.deleteById(t.getTid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the action of initiating a new treatment by selecting a patient and a caregiver from
     * the respective combo boxes. If both selections are valid, it opens a new treatment window
     * for the selected patient and caregiver. If either selection is missing, an informational alert
     * is displayed to guide the user to make the necessary selections.
     *
     * Exceptions:
     * - NullPointerException: This is caught and handled internally to show an alert
     *   when either the patient or caregiver selection is null.
     */
    @FXML
    public void handleNewTreatment() {
        try {
            String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
            String selectedCaregiver = this.comboBoxCaregiverSelection.getSelectionModel().getSelectedItem();
            Patient patient = getPersonFromDisplayName(selectedPatient, patientList);
            Caregiver caregiver = getPersonFromDisplayName(selectedCaregiver, caregiverList);
            newTreatmentWindow(patient, caregiver);
        } catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient oder Pfleger für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten und einen Pfleger aus!");
            alert.showAndWait();
        }
    }

    /**
     * Handles mouse click events on the table view. Specifically, it listens for double-click events
     * on the rows of the table. When a double-click occurs, and a valid table row is selected, it retrieves
     * the corresponding treatment from the list and opens a detailed treatment window.
     *
     * The method attaches an event listener to the table view.
     * If a row is double-clicked (click count of 2), it checks if there is a selected item and retrieves
     * the respective treatment based on the selected index in the table. It then calls the
     * {@code treatmentWindow} method to display additional information related to the selected treatment.
     */
    @FXML
    public void handleMouseClick() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.treatments.get(index);
                treatmentWindow(treatment);
            }
        });
    }

    /**
     * Opens a new treatment window to allow for the creation or viewing of treatments
     * for a specific patient and caregiver. The method initializes the UI elements
     * and sets up the controller with the provided data before displaying the window.
     *
     * @param patient The patient for whom the treatment is being created or viewed.
     * @param caregiver The caregiver associated with the treatment.
     */
    public void newTreatmentWindow(Patient patient, Caregiver caregiver) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();

            NewTreatmentPresenter controller = loader.getController();
            controller.initialize(this, stage, patient, caregiver);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Opens a new window that displays the treatment details and allows
     * interaction related to the specified treatment. The method sets up
     * the window layout using an FXML file and initializes its controller
     * with relevant data.
     *
     * @param treatment The {@code Treatment} object containing the details
     *                  of the treatment to be displayed in the window.
     */
    public void treatmentWindow(Treatment treatment) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            // the primary stage should stay in the background
            Stage stage = new Stage();
            TreatmentPresenter controller = loader.getController();
            controller.initializePresenter(this, stage, treatment);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
