package com.henri.client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.rmi.RemoteException;

/**
 * General send back class with several different implementations of the send back function
 * */
public class SendBack {

    /**
     * Function which enables the user to go back to the login screen
     * @param actionEvent The event tied to the go back button
     * */
    public void sendBackToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent gameDashboardPane = gameDashboardLoader.load();
        Scene gameDashboardScene = new Scene(gameDashboardPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScene);
    }

    /**
     * Function which enables the user to go back to the login screen
     * @param mouseEvent The event tied to the go back button
     * */
    public void sendBackToLogin(MouseEvent mouseEvent) throws IOException {
        FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/LoginScreen/Login.fxml"));
        Parent gameDashboardPane = gameDashboardLoader.load();
        Scene gameDashboardScene = new Scene(gameDashboardPane);

        Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScene);
    }


}
