package de.hitec.nhplus.presenter;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
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
 * The <code>AllTreatmentPresenter</code> contains the entire logic of the treatment view. It determines which data is displayed and how to react to events.
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

        this.createComboBoxDataForPatient();
        this.createComboBoxDataForCaregiver();

    }

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

    private void createComboBoxDataForPatient() {
        patientSelection.clear();
        patientSelection.add("alle");

        PatientDao dao = DaoFactory.getDaoFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            for (Patient patient : patientList) {
                this.patientSelection.add(formatPersonDisplayName(patient));
            }
            comboBoxPatientSelection.setItems(patientSelection);
            comboBoxPatientSelection.getSelectionModel().selectFirst(); // "alle" wird vorausgewählt
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void createComboBoxDataForCaregiver() {
        caregiverSelection.clear();
        caregiverSelection.add("alle");

        CaregiverDao dao = DaoFactory.getDaoFactory().createCaregiverDAO();
        try {
            caregiverList = (ArrayList<Caregiver>) dao.readAll();
            for (Caregiver caregiver : caregiverList) {
                this.caregiverSelection.add(formatPersonDisplayName(caregiver));
            }
            comboBoxCaregiverSelection.setItems(caregiverSelection);
            comboBoxCaregiverSelection.getSelectionModel().selectFirst(); // "alle" wird vorausgewählt
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleComboBoxes() {
        this.treatments.clear();
        List<Treatment> patientTreatments = handleComboBoxPatient();
        List<Treatment> caregiverTreatments = handleComboBoxCaregiver();

        for (Treatment treatment : patientTreatments) {
            for (Treatment treatmentCaregiver : caregiverTreatments) {
                if (treatmentCaregiver.getTid() == treatment.getTid()) {
                    this.treatments.add(treatment);
                }
            }
        }
    }

    public List<Treatment> handleComboBoxPatient() {
        String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
        this.dao = DaoFactory.getDaoFactory().createTreatmentDao();
        List<Treatment> patientTreatments = new ArrayList<>();
        try {
            if (selectedPatient == null || selectedPatient.equals("alle")) {
                patientTreatments.addAll(this.dao.readAll());
            } else {
                Patient patient = getPatientFromDisplayName(selectedPatient);
                if (patient != null) {
                    patientTreatments.addAll(this.dao.readTreatmentsByPid(patient.getPid()));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return patientTreatments;
    }

    public List<Treatment> handleComboBoxCaregiver() {
        String selectedCaregiver = this.comboBoxCaregiverSelection.getSelectionModel().getSelectedItem();
        List<Treatment> caregiverTreatments = new ArrayList<Treatment>();
        try {
            if (selectedCaregiver == null || selectedCaregiver.equals("alle")) {
                caregiverTreatments.addAll(this.dao.readAll());
            } else {
                Caregiver caregiver = getCaregiverFromDisplayName(selectedCaregiver);
                if (caregiver != null) {
                    caregiverTreatments.addAll(this.dao.readTreatmentsByCgid(caregiver.getCgID()));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return caregiverTreatments;

    }

    private String formatPersonDisplayName(Person person) {
        return String.format("%s, %s", person.getSurname(), person.getFirstName());
    }

    private Patient getPatientFromDisplayName(String displayName) {
        for (Patient patient : patientList) {
            if (displayName.equals(formatPersonDisplayName(patient))) {
                return patient;
            }
        }
        return null;
    }

    private Caregiver getCaregiverFromDisplayName(String displayName) {
        for (Caregiver caregiver : caregiverList) {
            if (displayName.equals(formatPersonDisplayName(caregiver))) {
                return caregiver;
            }
        }
        return null;
    }

    private Patient searchPatientInList(String name) {
        for (Patient patient : this.patientList) {
            if (formatPersonDisplayName(patient).equals(name)) {
                return patient;
            }
        }
        return null;
    }

    private Caregiver searchCaregiverInList(String name) {
        for (Caregiver caregiver : this.caregiverList) {
            if (formatPersonDisplayName(caregiver).equals(name)) {
                return caregiver;
            }
        }
        return null;
    }

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

    @FXML
    public void handleNewTreatment() {
        try {
            String selectedPatient = this.comboBoxPatientSelection.getSelectionModel().getSelectedItem();
            String selectedCaregiver = this.comboBoxCaregiverSelection.getSelectionModel().getSelectedItem();
            Patient patient = searchPatientInList(selectedPatient);
            Caregiver caregiver = searchCaregiverInList(selectedCaregiver);
            newTreatmentWindow(patient, caregiver);
        } catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient oder Pfleger für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten und einen Pfleger aus!");
            alert.showAndWait();
        }
    }

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
