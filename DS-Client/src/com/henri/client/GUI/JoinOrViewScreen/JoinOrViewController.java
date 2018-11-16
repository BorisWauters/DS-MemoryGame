package com.henri.client.GUI.JoinOrViewScreen;

import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;
import com.henri.client.GUI.GameScreen4X6.GameScreen4X6Controller;
import com.henri.client.GUI.GameScreen6X6.GameScreen6X6Controller;
import com.henri.client.GUI.MainClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinOrViewController {

    private int gameSize;
    private int gameId;


    @FXML
    private Button backButton;

    @FXML
    private Button joinButton;

    @FXML
    private Button viewButton;

    public void join(ActionEvent actionEvent) throws IOException {
        //when joining let user view and add him to the game for his turn (user cannot yet click on cards until it's his turn)
        //MainClient.impl.requestJoin(gameId,MainClient.username); -> add on initialize
        sendToGameScreen(actionEvent,false);

    }

    public void view(ActionEvent actionEvent){
        //let the player view, do not add him to the game, user cannot click any cards!
    }

    public void sendToGameScreen(ActionEvent actionEvent, boolean viewOnly) throws IOException {
        if(gameSize == 1){
            System.out.println("Game Size: 1");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X4/GameScreen4X4.fxml"));
            Parent gameScreenPane =  gameScreenLoader.load();
            Scene  gameScreenScene = new Scene( gameScreenPane);
            GameScreen4X4Controller gameScreen4X4Controller = gameScreenLoader.getController();
            gameScreen4X4Controller.setGameId(gameId);
            if(!viewOnly){
                gameScreen4X4Controller.setJoin(true);
            }
            if(viewOnly){
                gameScreen4X4Controller.setViewOnly(true);
            }


            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }else if(gameSize == 2){
            System.out.println("Game Size: 2");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen6X6/GameScreen6X6.fxml"));
            Parent gameScreenPane =  gameScreenLoader.load();
            Scene  gameScreenScene = new Scene( gameScreenPane);
            GameScreen6X6Controller gameScreen6X6Controller = gameScreenLoader.getController();
            gameScreen6X6Controller.setGameId(gameId);
            if(!viewOnly){
                gameScreen6X6Controller.setJoin(true);
            }
            if(viewOnly){
                gameScreen6X6Controller.setViewOnly(true);
            }

            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }else if(gameSize == 3){
            System.out.println("Game Size: 3");
            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameScreen4X6/GameScreen4X6.fxml"));
            Parent gameScreenPane =  gameScreenLoader.load();
            Scene  gameScreenScene = new Scene( gameScreenPane);
            GameScreen4X6Controller gameScreen4X6Controller = gameScreenLoader.getController();
            /*gameScreen4X6Controller.setGameId(gameId);
            if(!viewOnly){
                gameScreen4X6Controller.setJoin(true);
            }
            if(viewOnly){
                gameScreen4X6Controller.setViewOnly(true);
            }*/

            Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            primaryStage.setScene(gameScreenScene);
        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader homeScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/HomeScreen/HomeScreen.fxml"));
        Parent homeScreenPane = homeScreenLoader.load();
        Scene homeScreenScene = new Scene(homeScreenPane);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(homeScreenScene);
    }

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
}
