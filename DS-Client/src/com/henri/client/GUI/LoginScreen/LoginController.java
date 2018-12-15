package com.henri.client.GUI.LoginScreen;

import com.henri.client.GUI.GameDashboard.GameDashboardScreenController;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginController extends SendBack {

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

    @FXML
    private AnchorPane ap;

    /**
     * Function which acts on the event triggered by pressing on the login button
     * @param actionEvent The event tied to the login button
     * */
    public void login(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {

        String name = username.getText();
        String pass = password.getText();

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(pass.getBytes());
        String passwordHashed = new String(messageDigest.digest());

        String response = MainClient.impl.setupMessage(name, passwordHashed);


        if (response.equals("-1")) {
            credentialsLabel.setText("Credentials Incorrect");
            credentialsLabel.setVisible(true);
        } else {
            MainClient.username = username.getText();
            MainClient.userId = Integer.parseInt(response);
            String sessionConfig = MainClient.impl.acquireSessionId(name);
            ArrayList<String> sessionConfigList = new ArrayList<>(Arrays.asList(sessionConfig.split("\\s*,\\s*")));
            MainClient.sessionIdentifier_Id = Integer.parseInt(sessionConfigList.get(0));
            MainClient.sessionIdentifier = sessionConfigList.get(1);
            System.out.println("session id: " + MainClient.sessionIdentifier);

            FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
            Parent gameDashboardPane = gameDashboardLoader.load();
            Scene gameDashboardScene = new Scene(gameDashboardPane);

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameDashboardScene);
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
            //primaryStage.getIcons(new Image("com/henri/client/GUI/icon.png"));
            primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));
        }


    }

    /**
     * Function which enables the user to go bac to the home screen
     * @param actionEvent The event tied to the back button
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
