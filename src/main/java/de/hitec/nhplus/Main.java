package de.hitec.nhplus;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginWindow();
    }

    /**
     * Initializes and displays the user login window for the application.
     * This method sets up the JavaFX window using the "UserLoginView.fxml" file.
     * It configures the scene, disables resizing of the window, and sets the title to "NHPlus".
     *
     * Additionally, it handles application closure by closing the database connection,
     * exiting the JavaFX Platform, and terminating the JVM process when the window
     * close request is triggered.
     *
     * Any IOExceptions that occur during the loading of the FXML file are caught and
     * their stack traces are printed.
     */
    private void loginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/UserLoginView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            this.primaryStage.setTitle("NHPlus");
            this.primaryStage.setScene(scene);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();

            this.primaryStage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}