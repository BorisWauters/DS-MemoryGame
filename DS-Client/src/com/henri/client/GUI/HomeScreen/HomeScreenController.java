package com.henri.client.GUI.HomeScreen;


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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * Class which serves as the controller for the homescreen
 * */
public class HomeScreenController extends SendBack {


    @FXML
    private Button newUserButton;

    @FXML
    private Button existingUserButton;

    @FXML
    private AnchorPane ap;

    /**
     * Function which acts on the button "existing user" and loads the login page
     *
     * @param actionEvent The event tied to the button click
     * */
    public void existingUser(ActionEvent actionEvent) throws IOException {


        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


        //Initialize Login scene
        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent loginPane = loginLoader.load();
        Scene loginScene = new Scene(loginPane);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Memory Game");
        primaryStage.setOnCloseRequest(event -> {
            try {
                MainClient.implDispatch.remove(MainClient.clientId);
                Platform.exit();
                System.exit(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        //primaryStage.getIcons(new Image("com/henri/client/GUI/icon.png"));
        primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));


    }

    /**
     * Function which acts on the button "new user" and loads the registration page
     *
     * @param actionEvent The event tied to the button
     * */
    public void newUser(ActionEvent actionEvent) throws IOException {


        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        //Initialize NewUser scene
        FXMLLoader newUserLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/NewUserScreen/NewUserScreen.fxml"));
        Parent newUserPane = newUserLoader.load();
        Scene newUserScene = new Scene(newUserPane);

        primaryStage.setScene(newUserScene);


        primaryStage.setTitle("Memory Game");


    }
}
