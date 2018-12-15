package com.henri.client.GUI.GameScreen4X6;


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
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * Class which serves as the controller for the 4X6 game view
 * */
public class GameScreen4X6Controller extends GameScreen implements Initializable {

    private String path;

    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;

    @FXML
    private Button button11, button12, button13, button14, button15, button16, button17, button18, button19, button20;

    @FXML
    private Button button21, button22, button23, button24;

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

    /**
     * Function which initializes the view (load images, try joining the game, requesting game configuration..)
     * */
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



            mapButtonsToImagesGeneral();

            updateGameFieldGeneral();

            checkTurnGeneral(notYourTurnLabel);


            if (getCardsTurnedGuessedRightInTotal() == 24) {
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
                removeCallbackGeneral(getControllerId());
            });

            //Check if viewOnly
            if (isViewOnly()) {
                notYourTurnLabel.setText("You are in view only mode!");
            }

        });


    }

    /**
     * Function which updates a pressed button
     * @param buttonId The id of the button
     * */
    public void updateButton(int buttonId) {
        updateButtonGeneral(buttonId);
    }

    /**
     * Function which acts on a clicked button
     *
     * @param actionEvent The event which triggers the function
     * */
    public void buttonClicked(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(actionEvent);
        } else {
            buttonClickedGeneral(actionEvent, notYourTurnLabel);
        }


    }

    /**
     * Function which refreshes the screen
     * */
    public void refreshScreen() {
        Platform.runLater(() -> {
            try {
                refreshScreenGeneral(notYourTurnLabel, getGamePositions().size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Function which loads the images
     * */
    public void loadImages() {
        setImages(new ArrayList<>());
        if(getGameTheme() == 1){
            setClosedFile(new File(path + "\\DS-Client\\images-set1\\closed.png"));

            getImages().add(path + "\\DS-Client\\images-set1\\after_effects_logo.PNG");
            getImages().add(path + "\\DS-Client\\images-set1\\android_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\android_studio.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\apple_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\chrome_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\IntelliJ_IDEA_Logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\lightroom_logo.PNG");
            getImages().add(path + "\\DS-Client\\images-set1\\photoshop_logo.PNG");

            getImages().add(path + "\\DS-Client\\images-set1\\edge_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\firefox_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\react-native_logo.JPG");
            getImages().add(path + "\\DS-Client\\images-set1\\dell_logo.JPG");
        }else if(getGameTheme() == 2){
            setClosedFile(new File(path + "\\DS-Client\\images-set2\\back.jpg"));
            getImages().add(path + "\\DS-Client\\images-set2\\1.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\2.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\3.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\4.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\5.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\6.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\7.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\8.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\9.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\10.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\11.jpg");
            getImages().add(path + "\\DS-Client\\images-set2\\12.jpg");
        }

    }

    /**
     * Function which adds all buttons to a list
     * */
    public void addAllButtonsToList() {
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
        getButtons().add(button17);
        getButtons().add(button18);
        getButtons().add(button19);
        getButtons().add(button20);
        getButtons().add(button21);
        getButtons().add(button22);
        getButtons().add(button23);
        getButtons().add(button24);

    }

    /**
     * Function which registers a callback on the app server
     * */
    public void registerCallback() {
        try {
            setCallbackObj(new CallbackClientImpl(this));

            MainClient.impl.registerForCallback(getControllerId(), getCallbackObj(), getGameId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function which lets the user go back to the previous screen
     * */
    public void goBack(ActionEvent actionEvent) throws IOException {
        if (!MainClient.impl.checkSessionIdentifier(MainClient.sessionIdentifier_Id, MainClient.sessionIdentifier)) {
            sendBackToLogin(actionEvent);
        } else {
            goBackGeneral(actionEvent);
        }

    }

    /**
     * Function which sets the game theme text
     * */
    public String setGameThemeText(int gameTheme) {
        if (gameTheme == 1) {
            return "Software - theme";
        }
        return "World Peace - theme";
    }


}
