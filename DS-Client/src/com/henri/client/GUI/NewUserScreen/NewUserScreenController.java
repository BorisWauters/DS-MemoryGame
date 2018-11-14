package com.henri.client.GUI.NewUserScreen;

import com.henri.client.GUI.MainClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class NewUserScreenController {

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

    public void register(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException {
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String passwordCheckInput = passwordCheck.getText();

        if(!passwordInput.equals(passwordCheckInput)){
            credentialsLabel.setText("Passwords do not match");
            credentialsLabel.setVisible(true);
        }else if(MainClient.impl.checkUsername(usernameInput)){
            System.out.println("Username not taken");
            //username is not taken, proceed with registration
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(passwordInput.getBytes());
            String passwordHashed = new String(messageDigest.digest());
            MainClient.impl.registerUser(usernameInput,passwordHashed);

            //Initialize login screen
            FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
            Parent loginPane =  loginLoader.load();
            Scene  loginScene = new Scene( loginPane);

            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Memory Game");
            primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));



        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException{
        FXMLLoader homeScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/HomeScreen/HomeScreen.fxml"));
        Parent homeScreenPane = homeScreenLoader.load();
        Scene homeScreenScene = new Scene(homeScreenPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(homeScreenScene);
    }


}
