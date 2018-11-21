package com.henri.client.GUI.GameDashboard;

import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;
import com.henri.client.GUI.GameScreen4X6.GameScreen4X6Controller;
import com.henri.client.GUI.GameScreen6X6.GameScreen6X6Controller;
import com.henri.client.GUI.JoinOrViewScreen.JoinOrViewController;
import com.henri.client.GUI.MainClient;
import com.henri.client.GUI.SendBack;
import com.henri.client.GUI.tableItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;



public class GameDashboardScreenController extends SendBack implements Initializable{

    private Scene mScene;
    private ArrayList<Integer> gameSizesJoinedGames = new ArrayList<>();
    private ArrayList<Integer> numberOfPLayers = new ArrayList<>();
    private ArrayList<Integer> gameIdentifiers = new ArrayList<>();

    private ArrayList<Integer> gameSizesAllGames = new ArrayList<>();
    private ArrayList<Integer> gameIdentifiersAllGames = new ArrayList<>();
    private ArrayList<Integer> numberOfPlayersAllGames = new ArrayList<>();

    public void setScene(Scene mScene){this.mScene = mScene;}

    @FXML
    private Button newGame,refresh;

    @FXML
    private ListView playerGames;

    @FXML
    private ListView allGames;

    @FXML
    private TableView<tableItem> topPlayersTable;

    @FXML private TableColumn<tableItem,String> scoreColumn;
    @FXML private TableColumn<tableItem,String> usernameColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //get current games for the user
        requestGames();
        requestTopPlayers();
    }

    public void createObservable(ArrayList<String> playerGamesList, ObservableList<String> items, ArrayList<Integer> gameIdentifiers, ArrayList<Integer> numberOfPLayers, ArrayList<Integer> gameSizesJoinedGames) {
        for(int i = 0; i < playerGamesList.size(); i++){
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
            if(playerGamesList.get(i).equals("1")) sb.append("Software Theme");     //extract theme
            else if(playerGamesList.get(i).equals("2")) sb.append("Second theme");
            items.add(sb.toString());
            i++;
            sb.append(" | ");
            if(playerGamesList.get(i).equals("32")) {   //32 because 16 icons and 16 times true/false
                sb.append("4X4");
                gameSizesJoinedGames.add(1);
            }else if(playerGamesList.get(i).equals("72")){
                sb.append("6X6");
                gameSizesJoinedGames.add(2);
            }else if (playerGamesList.get(i).equals("48")) {
                sb.append("4X6");
                gameSizesJoinedGames.add(3);
            }

        }
    }


    public void newGame(ActionEvent actionEvent) throws IOException {
        if(!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)){
            sendBackToLogin(actionEvent);
        }else{
            FXMLLoader gameConfigLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameConfig/GameConfigScreen.fxml"));
            Parent gameConfigPane =  gameConfigLoader.load();
            Scene  gameConfigScene = new Scene( gameConfigPane);

            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameConfigScene);
        }

    }

    public void onMouseClickOwnGame(MouseEvent mouseEvent) throws IOException{

        if(!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)){
            sendBackToLogin(mouseEvent);
        }else{
            int gameSize = 0;
            if(playerGames.getSelectionModel().getSelectedIndex() >= 0){    //check whether a valid list item has been selected
                gameSize = gameSizesJoinedGames.get(playerGames.getSelectionModel().getSelectedIndex());
                int gameId = gameIdentifiers.get(playerGames.getSelectionModel().getSelectedIndex());
                sendToGameScreen(mouseEvent, gameSize, false, gameId);
            }
        }




    }

    public void onMouseClickAllGames(MouseEvent mouseEvent) throws IOException {
        //based on maxUserSize and current userSize, redirect to view only screen, or view or join inter-screen
        // If game is full, set view only!

        //See link for passing parameters to other controller: https://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
        System.out.println("function entered");
        //check number of players in the selected game
        if(allGames.getSelectionModel().getSelectedIndex() >= 0){
            int selectedListItem = allGames.getSelectionModel().getSelectedIndex();
            System.out.println(selectedListItem);
            int numberOfPlayersInGame = numberOfPlayersAllGames.get(2*selectedListItem);
            int maxNumberOfPlayers = (numberOfPlayersAllGames.get((2*selectedListItem)+1));
            int gameSize = gameSizesAllGames.get(selectedListItem);
            int gameId = gameIdentifiersAllGames.get(selectedListItem);

            //If game is full
            if(numberOfPlayersInGame == maxNumberOfPlayers){
                //send to view only screen

                sendToGameScreen(mouseEvent, gameSize, true, gameId);

            }else if(numberOfPlayersInGame < maxNumberOfPlayers){
                //if game is not full, send to decision screen: view or join
                FXMLLoader joinOrViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/JoinOrViewScreen/JoinOrViewScreen.fxml"));
                Parent joinOrViewScreenPane =  joinOrViewLoader.load();
                Scene joinOrViewScreenScene = new Scene( joinOrViewScreenPane);
                JoinOrViewController joinOrViewController = joinOrViewLoader.getController();
                joinOrViewController.setGameSize(gameSize);
                joinOrViewController.setGameId(gameId);

                Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(joinOrViewScreenScene);
            }
        }

    }

    public void sendToGameScreen(MouseEvent mouseEvent, int gameSize, boolean viewOnly, int gameId) throws IOException {
        if(gameSize == 1){
            System.out.println("Game Size: 1");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X4/GameScreen4X4.fxml"));

            Parent gameScreenPane =  gameScreenLoader.load();
            GameScreen4X4Controller gameScreen4X4Controller = gameScreenLoader.getController();
            gameScreen4X4Controller.setGameId(gameId);
            gameScreen4X4Controller.setControllerType(1);
            if(viewOnly){
                gameScreen4X4Controller.setViewOnly(true);
            }

            Scene  gameScreenScene = new Scene( gameScreenPane);



            Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }else if(gameSize == 2){
            System.out.println("Game Size: 2");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen6X6/GameScreen6X6.fxml"));
            Parent gameScreenPane =  gameScreenLoader.load();
            Scene  gameScreenScene = new Scene( gameScreenPane);
            GameScreen6X6Controller gameScreen6X6Controller = gameScreenLoader.getController();
            gameScreen6X6Controller.setGameId(gameId);
            gameScreen6X6Controller.setControllerType(2);
            if(viewOnly){
                gameScreen6X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }else if(gameSize == 3){
            System.out.println("Game Size: 3");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X6/GameScreen4X6.fxml"));
            Parent gameScreenPane =  gameScreenLoader.load();
            Scene  gameScreenScene = new Scene( gameScreenPane);
            GameScreen4X6Controller gameScreen4X6Controller = gameScreenLoader.getController();
            gameScreen4X6Controller.setGameId(gameId);
            gameScreen4X6Controller.setControllerType(3);
            if(viewOnly){
                gameScreen4X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException{
        FXMLLoader homeScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/HomeScreen/HomeScreen.fxml"));
        Parent homeScreenPane = homeScreenLoader.load();
        Scene homeScreenScene = new Scene(homeScreenPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(homeScreenScene);
    }

    public void requestGames(){
        try {
            ArrayList<String> playerGamesList = MainClient.impl.requestGames(MainClient.username);
            //items = createObservable(playerGamesList, gameSizesJoinedGames, numberOfPLayers, gameIdentifiers);

            ObservableList<String> items = FXCollections.observableArrayList();
            createObservable(playerGamesList, items, gameIdentifiers, numberOfPLayers, gameSizesJoinedGames);

            playerGames.setItems(items);
            System.out.println(items.toString());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Get all other games which the user hasn't joined
        try{
            ArrayList<String> playerGamesList = MainClient.impl.requestAllGames(MainClient.username);

            ObservableList<String> items = FXCollections.observableArrayList();
            createObservable(playerGamesList, items, gameIdentifiersAllGames, numberOfPlayersAllGames, gameSizesAllGames);


            allGames.setItems(items);

        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void refreshScreen(ActionEvent actionEvent){
        requestGames();
    }

    public void requestTopPlayers(){
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

        if(topPlayers != null){
            for(int i = 0; i < topPlayers.size(); i++){
                characters.add(new tableItem(topPlayers.get(i),(topPlayers.get(i+1))));
                i++;
            }
        }
        topPlayersTable.setItems(characters);
    }








}


