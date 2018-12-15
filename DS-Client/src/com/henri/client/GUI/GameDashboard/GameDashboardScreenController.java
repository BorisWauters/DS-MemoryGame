package com.henri.client.GUI.GameDashboard;

import com.henri.Server.AppServerInterface;
import com.henri.Server.Game;
import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;
import com.henri.client.GUI.GameScreen4X6.GameScreen4X6Controller;
import com.henri.client.GUI.GameScreen6X6.GameScreen6X6Controller;
import com.henri.client.GUI.JoinOrViewScreen.JoinOrViewController;
import com.henri.client.GUI.MainClient;
import com.henri.client.GUI.SendBack;
import com.henri.client.GUI.tableItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;


/**
 * Class which implements functions related to the game dashboard user interface.
 */
public class GameDashboardScreenController extends SendBack implements Initializable {

    private Scene mScene;
    private ArrayList<Integer> gameSizesJoinedGames = new ArrayList<>();
    private ArrayList<Integer> numberOfPLayers = new ArrayList<>();
    private ArrayList<Integer> gameIdentifiers = new ArrayList<>();
    private ArrayList<Integer> gameThemesJoinedGames = new ArrayList<>();

    private ArrayList<Integer> gameSizesAllGames = new ArrayList<>();
    private ArrayList<Integer> gameIdentifiersAllGames = new ArrayList<>();
    private ArrayList<Integer> numberOfPlayersAllGames = new ArrayList<>();
    private ArrayList<Integer> gameThemesAllGames = new ArrayList<>();
    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }


    @FXML
    private Button newGame, refresh;

    @FXML
    private ListView playerGames;

    @FXML
    private ListView allGames;

    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<tableItem> topPlayersTable;

    @FXML
    private TableColumn<tableItem, String> scoreColumn;
    @FXML
    private TableColumn<tableItem, String> usernameColumn;

    @FXML
    private AnchorPane ap;

    /**
     * {@inheritDoc} Function which, on initialization of the user interface, requests the games and the top players.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //get current games for the user
        requestGames();
        requestTopPlayers();
        usernameLabel.setText(" Welcome " + MainClient.username + " ");


    }

    /**
     * Function which creates entries for the ListView
     *
     * @param playerGamesList      List with the information about the game, such as id, name, active players
     * @param items                Observable list in which the entries get inserted
     * @param gameIdentifiers      New games id's get added to the general list of game identifiers
     * @param numberOfPLayers      List with each current completion of the number of user of each game
     * @param gameSizesJoinedGames List with all game sizes of joined games
     */
    public void createObservable(ArrayList<String> playerGamesList, ObservableList<String> items, ArrayList<Integer> gameIdentifiers, ArrayList<Integer> numberOfPLayers, ArrayList<Integer> gameSizesJoinedGames, ArrayList<Integer> gameThemes) {
        for (int i = 0; i < playerGamesList.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(playerGamesList.get(i));  //extract game id
            gameIdentifiers.add(Integer.parseInt(playerGamesList.get(i)));
            i++;
            sb.append(" | ");
            sb.append(playerGamesList.get(i));  //extract game name
            i++;
            sb.append(" | ");
            sb.append(playerGamesList.get(i));  //extract number of active players
            numberOfPLayers.add(Integer.parseInt(playerGamesList.get(i)));
            i++;
            sb.append("/");
            sb.append(playerGamesList.get(i));  //extract max number of players
            numberOfPLayers.add(Integer.parseInt(playerGamesList.get(i)));
            i++;
            sb.append(" | ");
            gameThemes.add(Integer.parseInt(playerGamesList.get(i)));
            if (playerGamesList.get(i).equals("1")) sb.append("Software Theme");     //extract theme
            else if (playerGamesList.get(i).equals("2")) sb.append("Second theme");
            items.add(sb.toString());
            i++;
            sb.append(" | ");
            if (playerGamesList.get(i).equals("32")) {   //32 because 16 icons and 16 times true/false
                sb.append("4X4");
                gameSizesJoinedGames.add(1);
            } else if (playerGamesList.get(i).equals("72")) {
                sb.append("6X6");
                gameSizesJoinedGames.add(2);
            } else if (playerGamesList.get(i).equals("48")) {
                sb.append("4X6");
                gameSizesJoinedGames.add(3);
            }

        }
    }


    /**
     * Function which send the user to the user interface where it can create a new game
     *
     * @param actionEvent The button which is clicked
     * @throws IOException
     * @see IOException
     */
    public void newGame(ActionEvent actionEvent) throws IOException {
        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(actionEvent);
        } else {
            FXMLLoader gameConfigLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameConfig/GameConfigScreen.fxml"));
            Parent gameConfigPane = gameConfigLoader.load();
            Scene gameConfigScene = new Scene(gameConfigPane);

            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setOnCloseRequest(event -> {
                try {
                    MainClient.implDispatch.remove(MainClient.clientId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.setScene(gameConfigScene);
        }

    }

    /**
     * Function which, when a user clicks on the ListView containing his own games, send the user to the game screen.
     *
     * @param mouseEvent Mouse click by the user
     * @throws IOException
     * @see IOException
     */
    public void onMouseClickOwnGame(MouseEvent mouseEvent) throws IOException {

        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(mouseEvent);
        } else {
            int gameSize = 0;
            if (playerGames.getSelectionModel().getSelectedIndex() >= 0) {    //check whether a valid list item has been selected
                gameSize = gameSizesJoinedGames.get(playerGames.getSelectionModel().getSelectedIndex());
                int gameId = gameIdentifiers.get(playerGames.getSelectionModel().getSelectedIndex());
                int gameTheme = gameThemesJoinedGames.get(playerGames.getSelectionModel().getSelectedIndex());
                sendToGameScreen(mouseEvent, gameSize, false, gameId, gameTheme);
            }
        }


    }


    //HOW TO: check database field of the game (save the port number of the appserver?). If game resides in an app server, return port and let user switch appservers.
    // If the game does not yet reside in an app server (e.g. new game which was created),
    // check if appserver on which the user currently is, is full (max 20 active games, save them in a Set). If full, switch to appserver which is not full.

    //This could be done automatically, or by forcing the user to log out and change server (maybe show all active servers on homescreen?)

    /**
     * Function which, when a user clicks on a game of which he is not part, send the user to the join or view screen.
     *
     * @param mouseEvent mouse click bu the user
     * @throws IOException
     * @see IOException
     */
    public void onMouseClickAllGames(MouseEvent mouseEvent) throws IOException {
        //based on maxUserSize and current userSize, redirect to view only screen, or view or join inter-screen
        // If game is full, set view only!

        //See link for passing parameters to other controller: https://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
        System.out.println("function entered");
        //check number of players in the selected game
        if (allGames.getSelectionModel().getSelectedIndex() >= 0) {
            int selectedListItem = allGames.getSelectionModel().getSelectedIndex();
            System.out.println(selectedListItem);
            int numberOfPlayersInGame = numberOfPlayersAllGames.get(2 * selectedListItem);
            int maxNumberOfPlayers = (numberOfPlayersAllGames.get((2 * selectedListItem) + 1));
            int gameSize = gameSizesAllGames.get(selectedListItem);
            int gameId = gameIdentifiersAllGames.get(selectedListItem);
            int gameTheme = gameThemesAllGames.get(selectedListItem);

            //If game is full
            if (numberOfPlayersInGame == maxNumberOfPlayers) {
                //send to view only screen

                sendToGameScreen(mouseEvent, gameSize, true, gameId, gameTheme);

            } else if (numberOfPlayersInGame < maxNumberOfPlayers) {
                //if game is not full, send to decision screen: view or join
                FXMLLoader joinOrViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/JoinOrViewScreen/JoinOrViewScreen.fxml"));
                Parent joinOrViewScreenPane = joinOrViewLoader.load();
                Scene joinOrViewScreenScene = new Scene(joinOrViewScreenPane);
                JoinOrViewController joinOrViewController = joinOrViewLoader.getController();
                joinOrViewController.setGameSize(gameSize);
                joinOrViewController.setGameId(gameId);
                joinOrViewController.setGameTheme(gameTheme);

                Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                primaryStage.setOnCloseRequest(event -> {
                    try {
                        MainClient.implDispatch.remove(MainClient.clientId);
                        Platform.exit();
                        System.exit(0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
                primaryStage.setScene(joinOrViewScreenScene);
            }
        }

    }

    /**
     * Function which send the user to the game screen given several input parameters.
     *
     * @param mouseEvent Click by the user, required to retrieve the current stage
     * @param gameSize   The size of the game, required to send the user to the correct user interface
     * @param viewOnly   True when the user cannot join, false when he can
     * @param gameId     The Id of the game the user would like to join
     * @throws IOException
     * @see IOException
     */
    public void sendToGameScreen(MouseEvent mouseEvent, int gameSize, boolean viewOnly, int gameId, int gameTheme) throws IOException {
        boolean isGameAvailable = MainClient.impl.checkGameAvailability(gameId);
        if(isGameAvailable){
            MainClient.game = MainClient.impl.enterGame(gameId);
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
                    MainClient.game = MainClient.impl.enterGame(gameId);
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
                MainClient.game =  MainClient.impl.enterNewGame(gameId);
            }
        }

        if (gameSize == 1) {
            System.out.println("Game Size: 1");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X4/GameScreen4X4.fxml"));

            Parent gameScreenPane = gameScreenLoader.load();
            GameScreen4X4Controller gameScreen4X4Controller = gameScreenLoader.getController();
            gameScreen4X4Controller.setGameId(gameId);
            gameScreen4X4Controller.setGameTheme(gameTheme);
            gameScreen4X4Controller.setControllerType(1);
            if (viewOnly) {
                gameScreen4X4Controller.setViewOnly(true);
            }

            Scene gameScreenScene = new Scene(gameScreenPane);


            Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

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
            if (viewOnly) {
                gameScreen6X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

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
            if (viewOnly) {
                gameScreen4X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            primaryStage.setScene(gameScreenScene);
        }
    }

    /**
     * Function which lets the user to go back to the previous user interface.
     *
     * @param actionEvent Button click by the user
     */
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

    /**
     * Function which requests the games of which the user is part of.
     */
    public void requestGames() {
        try {
            ArrayList<String> playerGamesList = MainClient.impl.requestGames(MainClient.username);
            //items = createObservable(playerGamesList, gameSizesJoinedGames, numberOfPLayers, gameIdentifiers);

            ObservableList<String> items = FXCollections.observableArrayList();
            createObservable(playerGamesList, items, gameIdentifiers, numberOfPLayers, gameSizesJoinedGames, gameThemesJoinedGames);

            playerGames.setItems(items);
            System.out.println(items.toString());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Get all other games which the user hasn't joined
        try {
            ArrayList<String> playerGamesList = MainClient.impl.requestAllGames(MainClient.username);

            ObservableList<String> items = FXCollections.observableArrayList();
            createObservable(playerGamesList, items, gameIdentifiersAllGames, numberOfPlayersAllGames, gameSizesAllGames, gameThemesAllGames);


            allGames.setItems(items);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function which refreshes the screen.
     *
     * @param actionEvent Button click by the user
     */
    public void refreshScreen(ActionEvent actionEvent) {
        requestGames();
    }

    /**
     * Function which requests the top players in the application.
     */
    public void requestTopPlayers() {
        //scoreColumn
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        //usernameColumn
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ObservableList<tableItem> characters = FXCollections.observableArrayList();
        ArrayList<String> topPlayers = null;
        try {
            topPlayers = MainClient.impl.requestTopPlayers();
        } catch (RemoteException e) {
            e.printStackTrace();
            //possibly set a label
        }

        if (topPlayers != null) {
            for (int i = 0; i < topPlayers.size(); i++) {
                characters.add(new tableItem(topPlayers.get(i), (topPlayers.get(i + 1))));
                i++;
            }
        }
        topPlayersTable.setItems(characters);
    }

}


