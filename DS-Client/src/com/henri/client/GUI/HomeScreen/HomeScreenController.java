package com.henri.client.GUI.HomeScreen;

import com.henri.client.GUI.GameDashboard.GameDashboardScreenController;
import com.henri.client.GUI.LoginScreen.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeScreenController {



    @FXML
    private Button newUserButton;

    @FXML
    private Button existingUserButton;

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
