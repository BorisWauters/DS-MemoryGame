package com.henri.client.GUI.JoinOrViewScreen;

import com.henri.Server.AppServerInterface;
import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;
import com.henri.client.GUI.GameScreen4X6.GameScreen4X6Controller;
import com.henri.client.GUI.GameScreen6X6.GameScreen6X6Controller;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class JoinOrViewController extends SendBack {

    private int gameSize;
    private int gameId, gameTheme;


    @FXML
    private Button backButton;

    @FXML
    private Button joinButton;

    @FXML
    private Button viewButton;

    @FXML
    private AnchorPane ap;

    /**
     * Function which acts on the "join" button when clicked
     * @param actionEvent The event tied to the button
     * */
    public void join(ActionEvent actionEvent) throws IOException {
        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(actionEvent);
        } else {
            //when joining let user view and add him to the game for his turn (user cannot yet click on cards until it's his turn)
            //MainClient.impl.requestJoin(gameId,MainClient.username); -> add on initialize
            sendToGameScreen(actionEvent, false);
        }


    }

    /**
     * Function which acts on the "view" button when clicked
     * @param actionEvent The event tied to the button
     * */
    public void view(ActionEvent actionEvent) throws IOException {
        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(actionEvent);
        } else {
            sendToGameScreen(actionEvent, true);
        }
        //let the player view, do not add him to the game, user cannot click any cards!
    }

    /**
     * Function which sends the user to the game screen based on a previous choice between join or view
     * @param actionEvent The vent tied to the buttons "join" or "view"
     * @param viewOnly Boolean which indicates whether the user should continue in view only mode
     * */
    public void sendToGameScreen(ActionEvent actionEvent, boolean viewOnly) throws IOException {
        // check if game is active on the app server or not!
        boolean isGameAvailable = MainClient.impl.checkGameAvailability(gameId);
        if(isGameAvailable){
            MainClient.impl.enterGame(gameId);
        }else{
            //game is not on server, check if the game is already on another server
            int gameOnOtherServerPort = MainClient.impl.checkGameOnOtherServer(gameId);
            if(gameOnOtherServerPort != -1){
                System.out.println(gameOnOtherServerPort);
                //game is active on another server, so we need to bind to that server

                //request move from dispatcher
                String serviceName = MainClient.implDispatch.requestMove(MainClient.clientId, gameOnOtherServerPort);
                try {
                    // fire to localhost on port
                    MainClient.registryServer = LocateRegistry.getRegistry("localhost", gameOnOtherServerPort);

                    // search AppServer
                    MainClient.impl = (AppServerInterface) MainClient.registryServer.lookup(serviceName);
                    //enter game
                    MainClient.impl.enterGame(gameId);
                    String sessionConfig = MainClient.impl.acquireSessionId(MainClient.username);
                    ArrayList<String> sessionConfigList = new ArrayList<>(Arrays.asList(sessionConfig.split("\\s*,\\s*")));
                    MainClient.sessionIdentifier_Id = Integer.parseInt(sessionConfigList.get(0));
                    MainClient.sessionIdentifier = sessionConfigList.get(1);
                    System.out.println("session id: " + MainClient.sessionIdentifier);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                //game is not assigned to any server yet, add it to this one
                MainClient.impl.enterNewGame(gameId);
            }
        }
        if (gameSize == 1) {
            System.out.println("Game Size: 1");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X4/GameScreen4X4.fxml"));
            Parent gameScreenPane = gameScreenLoader.load();
            Scene gameScreenScene = new Scene(gameScreenPane);
            GameScreen4X4Controller gameScreen4X4Controller = gameScreenLoader.getController();
            gameScreen4X4Controller.setGameId(gameId);
            gameScreen4X4Controller.setControllerType(1);
            gameScreen4X4Controller.setGameTheme(gameTheme);
            if (!viewOnly) {
                gameScreen4X4Controller.setJoin(true);
            }
            if (viewOnly) {
                gameScreen4X4Controller.setViewOnly(true);
            }


            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> {
                try {
                    MainClient.implDispatch.remove(MainClient.clientId);
                    Platform.exit();
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.setScene(gameScreenScene);
        } else if (gameSize == 2) {
            System.out.println("Game Size: 2");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen6X6/GameScreen6X6.fxml"));
            Parent gameScreenPane = gameScreenLoader.load();
            Scene gameScreenScene = new Scene(gameScreenPane);
            GameScreen6X6Controller gameScreen6X6Controller = gameScreenLoader.getController();
            gameScreen6X6Controller.setGameId(gameId);
            gameScreen6X6Controller.setControllerType(2);
            gameScreen6X6Controller.setGameTheme(gameTheme);
            if (!viewOnly) {
                gameScreen6X6Controller.setJoin(true);
            }
            if (viewOnly) {
                gameScreen6X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> {
                try {
                    MainClient.implDispatch.remove(MainClient.clientId);
                    Platform.exit();
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.setScene(gameScreenScene);
        } else if (gameSize == 3) {
            System.out.println("Game Size: 3");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X6/GameScreen4X6.fxml"));
            Parent gameScreenPane = gameScreenLoader.load();
            Scene gameScreenScene = new Scene(gameScreenPane);
            GameScreen4X6Controller gameScreen4X6Controller = gameScreenLoader.getController();
            gameScreen4X6Controller.setGameId(gameId);
            gameScreen4X6Controller.setControllerType(3);
            gameScreen4X6Controller.setGameTheme(gameTheme);
            if (!viewOnly) {
                gameScreen4X6Controller.setJoin(true);
            }
            if (viewOnly) {
                gameScreen4X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> {
                try {
                    MainClient.implDispatch.remove(MainClient.clientId);
                    Platform.exit();
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.setScene(gameScreenScene);
        }
    }

    /**
     * Function which lets the user go back the the game dashboard
     * @param actionEvent The event tied to the button
     * */
    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader dashboardScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
        Parent dashboardScreenPane = dashboardScreenLoader.load();
        Scene dashboardScreenScene = new Scene(dashboardScreenPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setOnCloseRequest(event -> {
            try {
                MainClient.implDispatch.remove(MainClient.clientId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        primaryStage.setScene(dashboardScreenScene);
    }

    // getters and setters
    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameTheme() {
        return gameTheme;
    }

    public void setGameTheme(int gameTheme) {
        this.gameTheme = gameTheme;
    }
}
