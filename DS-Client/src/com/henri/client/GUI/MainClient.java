package com.henri.client.GUI;

import com.henri.Dispatcher.DispatcherInterface;
import com.henri.RMI.Server.AppServerInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;


public class MainClient extends Application {

    public static AppServerInterface impl;
    public static DispatcherInterface implDispatch;
    public static String sessionIdentifier;
    public static String username;
    public static int sessionIdentifier_Id;
    public static int clientId;

    /**
     * Method used to display HomeScreen.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Initialize first scene
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("./HomeScreen/HomeScreen.fxml"));
        Parent homePane = homeLoader.load();
        Scene homeScene = new Scene(homePane);


        primaryStage.setTitle("Memory Game");
        primaryStage.getIcons().add(new Image("com/henri/client/GUI/icon.png"));
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void startClient() throws RemoteException {
        Random rand = new Random();
        clientId = rand.nextInt(100000);

        //contact Dispatcher
        try {
            // fire to localhost on port 1103
            Registry registryDispatcherServer = LocateRegistry.getRegistry("localhost", 1103);

            // search Dispatcher
            implDispatch = (DispatcherInterface) registryDispatcherServer.lookup("DispatcherImplService");


        } catch (Exception e) {
            e.printStackTrace();
        }

        //request AppServer to connect to
        int port = implDispatch.setup(clientId);

        try {
            // fire to localhost on port 1099
            Registry registryServer = LocateRegistry.getRegistry("localhost", port);

            // search AppServer
            impl = (AppServerInterface) registryServer.lookup("AppServerImplService");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MainClient method.
     *
     * @param args
     */
    public static void main(String[] args) throws RemoteException {
        MainClient mainClient = new MainClient();
        mainClient.startClient();
        launch(args);

    }
}
