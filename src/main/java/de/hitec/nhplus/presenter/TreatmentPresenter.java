package de.hitec.nhplus.presenter;

import de.hitec.nhplus.datastorage.CaregiverDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;
import de.hitec.nhplus.model.Caregiver;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * The TreatmentPresenter class manages the presentation layer for displaying
 * and modifying treatment details. It serves as a controller for the corresponding
 * JavaFX view and interacts with the model layer to fetch, display, and update
 * patient, caregiver, and treatment data.
 *
 * It utilizes Data Access Objects (DAOs) to retrieve and persist information
 * to the underlying database. The class includes functionality to initialize
 * the presenter with the necessary dependencies, populate UI components with
 * data, handle user actions, and update treatment details.
 */
public class TreatmentPresenter {

    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelCareLevel;

    @FXML
    private Label labelCaregiverName;

    @FXML
    private Label labelCaregiverTel;

    @FXML
    private TextField textFieldBegin;

    @FXML
    private TextField textFieldEnd;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextArea textAreaRemarks;

    @FXML
    private DatePicker datePicker;

    private AllTreatmentPresenter presenter;
    private Stage stage;
    private Patient patient;
    private Caregiver caregiver;
    private Treatment treatment;

    /**
     * Initializes the presenter for the treatment view, sets up the stage, retrieves patient
     * and caregiver data from the database, and displays the relevant information.
     *
     * @param presenter the {@code AllTreatmentPresenter} responsible for managing the treatment view
     * @param stage the {@code Stage} on which the treatment data is displayed
     * @param treatment the {@code Treatment} object containing details of the treatment to be presented
     */
    public void initializePresenter(AllTreatmentPresenter presenter, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.presenter = presenter;
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDao();
        CaregiverDao cDao = DaoFactory.getDaoFactory().createCaregiverDao();
        try {
            this.treatment = treatment;
            this.patient = pDao.read((int) treatment.getPid());
            this.caregiver = cDao.read((int) treatment.getCgid());
            showData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Populates the user interface fields with data from the associated patient, caregiver,
     * and treatment objects, allowing the user to view and edit the treatment details.
     *
     * The method sets the text and values of various UI components, such as labels, text fields,
     * and date picker, based on the properties of the patient, caregiver, and treatment instances.
     * It uses an external utility class to handle date conversion.
     *
     * The following steps are performed:
     * - Sets the patient's name and care level in the respective UI labels.
     * - Displays the caregiver's name and contact number in the respective UI labels.
     * - Converts the treatment date to a LocalDate and sets it in the date picker.
     * - Sets the treatment's start time, end time, description, and remarks in the corresponding
     *   UI text fields and text area.
     */
    private void showData(){
        this.labelPatientName.setText(patient.getSurname()+", "+patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        this.labelCaregiverName.setText(caregiver.getSurname()+", "+caregiver.getFirstName());
        this.labelCaregiverTel.setText(caregiver.getTelNumber());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datePicker.setValue(date);
        this.textFieldBegin.setText(this.treatment.getBegin());
        this.textFieldEnd.setText(this.treatment.getEnd());
        this.textFieldDescription.setText(this.treatment.getDescription());
        this.textAreaRemarks.setText(this.treatment.getRemarks());
    }

    /**
     * Handles the "change" event when triggered in the GUI. Updates the treatment
     * details with new data from the corresponding UI input fields (date, begin time,
     * end time, description, and remarks). After updating the treatment, it performs
     * the following actions:
     *
     * - Calls the `doUpdate` method to persist the changes to the database.
     * - Triggers the presenter to fetch all treatments and refresh the TableView display.
     * - Closes the current stage.
     *
     * This method ensures that the treatment details are updated and the UI reflects
     * the most recent data.
     */
    @FXML
    public void handleChange(){
        this.treatment.setDate(this.datePicker.getValue().toString());
        this.treatment.setBegin(textFieldBegin.getText());
        this.treatment.setEnd(textFieldEnd.getText());
        this.treatment.setDescription(textFieldDescription.getText());
        this.treatment.setRemarks(textAreaRemarks.getText());
        doUpdate();
        presenter.readAllAndShowInTableView();
        stage.close();
    }

    /**
     * Updates the current treatment information in the database.
     *
     * This method utilizes the TreatmentDao to perform an update operation on
     * the treatment object. It retrieves the DAO instance from the DaoFactory
     * and executes the update method. If an SQLException occurs during the
     * update, the exception stack trace is printed to the console.
     */
    private void doUpdate(){
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.update(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the cancel action triggered by the user.
     * This method closes the current stage, effectively dismissing the
     * current window or dialog associated with the treatment presenter.
     */
    @FXML
    public void handleCancel(){
        stage.close();
    }
}