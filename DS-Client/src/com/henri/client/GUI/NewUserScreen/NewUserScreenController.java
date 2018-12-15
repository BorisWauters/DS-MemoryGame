package com.henri.client.GUI.NewUserScreen;

import com.henri.client.GUI.MainClient;
import com.henri.client.GUI.SendBack;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;


public class NewUserScreenController extends SendBack {

    private Scene mScene;

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordCheck;

    @FXML
    private Label credentialsLabel;

    @FXML
    private Button backButton;

    @FXML
    private AnchorPane ap;

    /**
     * Function which acts on the register button when clicked
     * @param actionEvent The event tied to the button
     * */
    public void register(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException {
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String passwordCheckInput = passwordCheck.getText();

        if (!passwordInput.equals(passwordCheckInput)) {
            credentialsLabel.setText("Passwords do not match");
            credentialsLabel.setVisible(true);
        } else if (MainClient.impl.checkUsername(usernameInput)) {
            System.out.println("Username not taken");
            //username is not taken, proceed with registration
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(passwordInput.getBytes());
            String passwordHashed = new String(messageDigest.digest());
            MainClient.impl.registerUser(usernameInput, passwordHashed);

            //Initialize login screen
            FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
            Parent loginPane = loginLoader.load();
            Scene loginScene = new Scene(loginPane);

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(loginScene);
            primaryStage.setOnCloseRequest(event -> {
                try {
                    MainClient.implDispatch.remove(MainClient.clientId);
                    Platform.exit();
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.setTitle("Memory Game");
            primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));


        }
    }

    /**
     * Funcion which enables the user to go back to the home screen
     * @param actionEvent The event tied to the button
     * */
    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader homeScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/HomeScreen/HomeScreen.fxml"));
        Parent homeScreenPane = homeScreenLoader.load();
        Scene homeScreenScene = new Scene(homeScreenPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setOnCloseRequest(event -> {
            try {
                MainClient.implDispatch.remove(MainClient.clientId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        primaryStage.setScene(homeScreenScene);
    }


}
