package com.henri.client.GUI;

import com.henri.Dispatcher.DispatcherInterface;
import com.henri.Server.AppServerInterface;
import com.henri.Server.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class which starts the client application
 * */
public class MainClient extends Application {

    public static AppServerInterface impl;
    public static DispatcherInterface implDispatch;
    public static Registry registryServer;
    public static String sessionIdentifier;
    public static String username;
    public static int userId;
    public static int sessionIdentifier_Id;
    public static int clientId;
    public static Game game;

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
        primaryStage.setOnCloseRequest(event -> {
            try {
                MainClient.implDispatch.remove(MainClient.clientId);
                Platform.exit();
                System.exit(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        primaryStage.show();
    }

    /**
     * Function which creates the RMI communication channels
     * */
    private void startClient() throws RemoteException, NotBoundException {
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
        ArrayList<Integer> result = implDispatch.setup(clientId);

        try {
            // fire to localhost on port 1099
            registryServer = LocateRegistry.getRegistry("localhost", result.get(0));

            // search AppServer
            impl = (AppServerInterface) registryServer.lookup(String.valueOf(result.get(1)));
            System.out.println("CHECK: app server: " + result.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MainClient method.
     *
     * @param args
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        MainClient mainClient = new MainClient();
        mainClient.startClient();
        launch(args);

    }
}
