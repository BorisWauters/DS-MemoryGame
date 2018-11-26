package com.henri.client.GUI.HomeScreen;


import com.henri.client.GUI.MainClient;
import com.henri.client.GUI.SendBack;
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
import java.util.ResourceBundle;


public class HomeScreenController  extends SendBack implements Initializable {



    @FXML
    private Button newUserButton;

    @FXML
    private Button existingUserButton;

    @FXML
    private AnchorPane ap;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        onClose(ap, MainClient.clientId);
    }

    public void existingUser(ActionEvent actionEvent) throws IOException {




        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();



        //Initialize Login scene
        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent loginPane = loginLoader.load();
        Scene loginScene = new Scene(loginPane);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Memory Game");
        //primaryStage.getIcons(new Image("com/henri/client/GUI/icon.png"));
        primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));








    }

    public void newUser(ActionEvent actionEvent) throws IOException {


        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        //Initialize NewUser scene
        FXMLLoader newUserLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/NewUserScreen/NewUserScreen.fxml"));
        Parent newUserPane = newUserLoader.load();
        Scene newUserScene = new Scene(newUserPane);

        primaryStage.setScene(newUserScene);


        primaryStage.setTitle("Memory Game");





    }
}
