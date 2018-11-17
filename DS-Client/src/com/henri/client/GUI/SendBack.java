package com.henri.client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class SendBack {

    public void sendBackToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent gameDashboardPane =  gameDashboardLoader.load();
        Scene gameDashboardScene = new Scene( gameDashboardPane);

        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScene);
    }

    public void sendBackToLogin(MouseEvent mouseEvent) throws IOException {
        FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent gameDashboardPane =  gameDashboardLoader.load();
        Scene gameDashboardScene = new Scene( gameDashboardPane);

        Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScene);
    }
}
