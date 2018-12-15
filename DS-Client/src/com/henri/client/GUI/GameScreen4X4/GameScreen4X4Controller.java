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

/**
 *
 * Class which serves as the controller for the 4X4 game view
 * */
public class GameScreen4X4Controller extends GameScreen implements Initializable {

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
                removeCallbackGeneral(getControllerId());
                try{
                    MainClient.implDispatch.remove(MainClient.clientId);
                    MainClient.impl.leaveGame(getGameId());
                    Platform.exit();
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

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
