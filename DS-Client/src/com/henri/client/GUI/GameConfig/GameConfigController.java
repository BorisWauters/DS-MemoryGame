package com.henri.client.GUI.GameConfig;

import com.henri.client.GUI.MainClient;
import com.henri.client.GUI.SendBack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GameConfigController extends SendBack {

    private int numberOfPlayers;
    private int gameSize; // 1 = 4X4; 2 = 6X6; 3 = 4X6
    private int gameTheme; // software theme = 1; second theme = 2;

    @FXML
    private CheckBox twoPlayers;

    @FXML
    private CheckBox threePlayers;

    @FXML
    private CheckBox fourPlayers;

    @FXML
    private Label playerLabel;

    @FXML
    private CheckBox fourByFour;

    @FXML
    private CheckBox sixBySix;

    @FXML
    private CheckBox fourBySix;

    @FXML
    private Label sizeLabel;

    @FXML
    private  CheckBox softwareTheme;

    @FXML
    private CheckBox secondTheme;

    @FXML
    private Label themeLabel, gameLabel;

    @FXML
    private Button createGame;

    @FXML
    private TextField gameName;

    public void newGame(ActionEvent actionEvent) throws IOException {

        if(!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)){
            sendBackToLogin(actionEvent);
        }
        else{
            //Checking if only one checkbox per section has been marked

            boolean inputCorrect = true;

            if((twoPlayers.isSelected() && threePlayers.isSelected()) || (threePlayers.isSelected() && fourPlayers.isSelected()) || (twoPlayers.isSelected() && fourPlayers.isSelected())){
                playerLabel.setText(" Only select one box! ");
                playerLabel.setVisible(true);
                inputCorrect = false;
            }if((fourByFour.isSelected() && sixBySix.isSelected()) || (sixBySix.isSelected() && fourBySix.isSelected()) || (fourByFour.isSelected() && fourBySix.isSelected())){
                sizeLabel.setText(" Only select one box! ");
                sizeLabel.setVisible(true);
                inputCorrect = false;
            }if((softwareTheme.isSelected() && secondTheme.isSelected())){
                themeLabel.setText(" Only select one box! ");
                themeLabel.setVisible(true);
                inputCorrect = false;
            }if(gameName.getText().isEmpty()){
                gameLabel.setText(" Enter a game name! ");
                inputCorrect = false;
            }
            if(inputCorrect){
                //initiate game on app server
                if(twoPlayers.isSelected()) numberOfPlayers = 2;
                else if(threePlayers.isSelected()) numberOfPlayers = 3;
                else if(fourPlayers.isSelected()) numberOfPlayers = 4;

                if(fourByFour.isSelected()) gameSize = 1;
                else if(sixBySix.isSelected()) gameSize = 2;
                else if(fourBySix.isSelected()) gameSize = 3;

                if(softwareTheme.isSelected()) gameTheme = 1;
                else if(secondTheme.isSelected()) gameTheme = 2;

                StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf(numberOfPlayers));
                sb.append(",");
                sb.append(String.valueOf(gameSize));
                sb.append(",");
                sb.append(String.valueOf(gameTheme));
                sb.append(",");
                sb.append(gameName.getText());
                MainClient.impl.createGame(sb.toString(),MainClient.username);

                FXMLLoader gameDashboardLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
                Parent gameDashboardPane =  gameDashboardLoader.load();
                Scene gameDashboardScene = new Scene( gameDashboardPane);

                Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(gameDashboardScene);
            }
        }


    }




}
