package com.henri.client.GUI.GameScreen4X4;

import com.henri.client.GUI.GameScreen.GameScreen;
import com.henri.client.GUI.MainClient;
import com.henri.client.RMI.CallbackClientImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class GameScreen4X4Controller extends GameScreen implements Initializable{

    private String path;


    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;

    @FXML
    private Button button11, button12, button13, button14, button15, button16;

    @FXML
    private AnchorPane ap;

    @FXML
    private Label notYourTurnLabel;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label gameThemeLabel;

    @FXML
    private Button backButton;

    //ToDo: vaste paden nar relatieve paden omzetten

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {

            //Give the controller an ID
            Random rand = new Random();
            setControllerId(rand.nextInt(100000));

            path = System.getProperty("user.dir");

            loadImages();

            addAllButtonsToList();

            tryJoinGeneral();

            requestGameConfigGeneral();


            gameNameLabel.setText(getGameName());
            gameThemeLabel.setText(setGameThemeText(getGameTheme()));

            setClosedFile(new File(path + "\\DS-Client\\src\\com\\henri\\client\\GUI\\GameScreen4X4\\closed.png"));

            //setClosedFile(new File("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\src\\com\\henri\\client\\GUI\\GameScreen4X4\\closed.png"));

            mapButtonsToImagesGeneral();

            updateGameFieldGeneral();

            checkTurnGeneral(notYourTurnLabel);


            if (getCardsTurnedGuessedRightInTotal() == 16) {
                setWinner(notYourTurnLabel);

            }


            //Check if at least two users are registerd
            if (getNumberOfUsers() < 2) {
                for (Button b : getButtons()) {
                    b.setDisable(true);
                }
                notYourTurnLabel.setText("Not enough players! Need at least 2!");
            }

            //Register callback for live updates
            registerCallback();


            //Set new on close handler to remove callback from app server
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                removeCallbackGeneral(stage, getControllerId());
            });

            //Check if viewOnly
            if(isViewOnly()){
                notYourTurnLabel.setText("You are in view only mode!");
            }

        });


    }




    public void updateButton(int buttonId) {

        updateButtonGeneral(buttonId);
    }

    public void buttonClicked(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)){
            sendBackToLogin(actionEvent);
        }else{
            buttonClickedGeneral(actionEvent, notYourTurnLabel);
        }



    }


    public void refreshScreen(){
        Platform.runLater(() -> {
            try {
                refreshScreenGeneral(notYourTurnLabel,getGamePositions().size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    public void loadImages(){
        setImages(new ArrayList<>());
        getImages().add(path + "\\DS-Client\\images-set1\\after_effects_logo.PNG");
        getImages().add(path + "\\DS-Client\\images-set1\\android_logo.JPG");
        getImages().add(path + "\\DS-Client\\images-set1\\android_studio.JPG");
        getImages().add(path + "\\DS-Client\\images-set1\\apple_logo.JPG");
        getImages().add(path + "\\DS-Client\\images-set1\\chrome_logo.JPG");
        getImages().add(path + "\\DS-Client\\images-set1\\IntelliJ_IDEA_Logo.JPG");
        getImages().add(path + "\\DS-Client\\images-set1\\lightroom_logo.PNG");
        getImages().add(path + "\\DS-Client\\images-set1\\photoshop_logo.PNG");
    }

    public void addAllButtonsToList(){
        setButtons(new ArrayList<>());
        getButtons().add(button1);
        getButtons().add(button2);
        getButtons().add(button3);
        getButtons().add(button4);
        getButtons().add(button5);
        getButtons().add(button6);
        getButtons().add(button7);
        getButtons().add(button8);
        getButtons().add(button9);
        getButtons().add(button10);
        getButtons().add(button11);
        getButtons().add(button12);
        getButtons().add(button13);
        getButtons().add(button14);
        getButtons().add(button15);
        getButtons().add(button16);
    }








    public void registerCallback(){
        try {
            setCallbackObj(new CallbackClientImpl(this));

            MainClient.impl.registerForCallback(getControllerId(), getCallbackObj(), getGameId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void goBack(ActionEvent actionEvent) throws IOException {
        if(!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)){
            sendBackToLogin(actionEvent);
        }else{
            goBackGeneral(actionEvent);
        }

    }

    public String setGameThemeText(int gameTheme) {
        if (gameTheme == 1) {
            return "Software - theme";
        }
        return "Second - theme";
    }




}
