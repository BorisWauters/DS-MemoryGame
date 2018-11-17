package com.henri.client.GUI.LoginScreen;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginController {

    private Scene gameDashboardScene;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label credentialsLabel;

    @FXML
    private Button backButton;


    public void setGameScene(Scene gameScene) {
        this.gameDashboardScene = gameScene;
    }

    public void login(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {

        String name = username.getText();
        String pass = password.getText();

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(pass.getBytes());
        String passwordHashed = new String(messageDigest.digest());

        String response = MainClient.impl.setupMessage(name, passwordHashed);


        if (response.equals("false")) {
            credentialsLabel.setText("Credentials Incorrect");
            credentialsLabel.setVisible(true);
        } else {
            MainClient.username = username.getText();
            String sessionConfig = MainClient.impl.acquireSessionId(name);
            ArrayList<String> sessionConfigList = new ArrayList<>(Arrays.asList(sessionConfig.split("\\s*,\\s*")));
            MainClient.sessionIdentifier = sessionConfigList.get(0);
            MainClient.sessionIdentifier_Id = Integer.parseInt(sessionConfigList.get(1));
            System.out.println("session id: " + MainClient.sessionIdentifier);

            FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
            Parent gameDashboardPane = gameDashboardLoader.load();
            Scene gameDashboardScene = new Scene(gameDashboardPane);

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameDashboardScene);
            primaryStage.setTitle("Memory Game");
            //primaryStage.getIcons(new Image("com/henri/client/GUI/icon.png"));
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
