package de.hitec.nhplus.presenter;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.UserDao;
import de.hitec.nhplus.model.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * The UserLoginPresenter class is responsible for handling the user login functionality
 * and managing the login UI components. It coordinates interactions between the UI and
 * the backend services to validate user credentials and control the navigation to the
 * main application window.
 */
public class UserLoginPresenter {
    @FXML
    private TextField username;

    @FXML
    private TextField userPassword;

    @FXML
    private Button buttonLogin;


    /**
     * Initializes the UI components when the view is loaded.
     * Initially disables the login button and adds listeners
     * to the username and password fields to enable the button
     * only when both fields contain valid input.
     */
    public void initialize(){
        this.buttonLogin.setDisable(true);
        ChangeListener<String> inputLoginListener = (observableValue, oldText, newText) ->
                UserLoginPresenter.this.buttonLogin.setDisable(areInputDataInvalid());
        this.username.textProperty().addListener(inputLoginListener);
        this.userPassword.textProperty().addListener(inputLoginListener);
    }

    /**
     * Checks whether the input data is invalid.
     * Input is considered invalid if the username or password is null,
     * empty, or contains only whitespace.
     *
     * @return true if either the username or password field is empty or invalid; false otherwise
     */
    private boolean areInputDataInvalid() {
        return this.username.getText().isEmpty() || this.userPassword.getText().isEmpty();
    }

    /**
     * Handles the login button click event.
     * Retrieves and trims the input data from the UI, and checks credentials.
     * If correct, proceeds to the main application window.
     * Otherwise, displays an alert with an error message.
     */
    @FXML
    public void handleLogin(){
        String name = this.username.getText().trim();
        String password = this.userPassword.getText();

        if(areInputDataCorrect(name, password)){
            mainWindow();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Benutzername oder/und Passwort sind falsch!");
            alert.setContentText("Tragen Sie die korrekten Daten in die Felder ein!");
            alert.showAndWait();
        }
    }

    /**
     * Initializes and displays the main application window.
     * Sets the stage title, scene, and disables stage resizing.
     * Loads the main window view from the specified FXML resource.
     * Registers a handler for the stage's close request event to ensure proper application shutdown
     * by closing database connections and terminating the application.
     *
     * Handles potential IOException that could occur during FXML loading.
     */
    private void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Verifies if the provided name and password match a user stored in the database.
     *
     * @param name the name of the user to validate
     * @param password the password of the user to validate
     * @return true if a user with the given name and password exists in the database; false otherwise
     */
    private boolean areInputDataCorrect(String name, String password) {
        UserDao dao = DaoFactory.getDaoFactory().createUserDao();
        List<User> allUsers;
        try {
            allUsers = dao.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (User user : allUsers){
            if (user.getName().equals(name) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

}