package com.henri.client.GUI.GameScreen;

import com.henri.client.GUI.MainClient;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.File;
import java.rmi.RemoteException;
import java.util.*;

public class GameScreen {

    //Algemene klasse waar de controllers kunnen van overerven

    private int controllerId;
    private boolean viewOnly = false;

    private int gameId, numberOfUsers, gameTheme, maxUserAllowed;
    private String gameName;

    private boolean join = false;
    private boolean myTurn = false;

    private ArrayList<String> gamePositions;
    private ArrayList<Button> buttons;
    private ArrayList<String> images;
    private Map<Integer, String> buttonToFileMapping = new HashMap<>();
    private Set<Integer> correctGuesses = new HashSet<>();

    private int cardsTurnedGuessedRightInTotal;

    private File closedFile;

    private Button pressedButton = null;

    public static void removeCallback(Stage stage, int controllerId) {
        stage.setOnCloseRequest(event -> {
            try {
                MainClient.impl.removeCallback(controllerId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });
    }

    public static void updateButton(int buttonId, ArrayList<Button> buttons, Map<Integer, String> buttonToFileMapping) {
        for (Button b : buttons) {
            if (Integer.parseInt(b.getId()) == buttonId) {
                Platform.runLater(() -> {
                    File f2 = new File(buttonToFileMapping.get(buttonId));
                    Image image = new Image(f2.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(90);
                    b.setStyle("-fx-background-color: #FFFFFF");
                    b.setGraphic(imageView);

                });
            }
        }
    }

    //GETTERS AND SETTERS


    public int getControllerId() {
        return controllerId;
    }

    public void setControllerId(int controllerId) {
        this.controllerId = controllerId;
    }

    public boolean isViewOnly() {
        return viewOnly;
    }

    public void setViewOnly(boolean viewOnly) {
        this.viewOnly = viewOnly;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public int getGameTheme() {
        return gameTheme;
    }

    public void setGameTheme(int gameTheme) {
        this.gameTheme = gameTheme;
    }

    public int getMaxUserAllowed() {
        return maxUserAllowed;
    }

    public void setMaxUserAllowed(int maxUserAllowed) {
        this.maxUserAllowed = maxUserAllowed;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public ArrayList<String> getGamePositions() {
        return gamePositions;
    }

    public void setGamePositions(ArrayList<String> gamePositions) {
        this.gamePositions = gamePositions;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Map<Integer, String> getButtonToFileMapping() {
        return buttonToFileMapping;
    }

    public void setButtonToFileMapping(Map<Integer, String> buttonToFileMapping) {
        this.buttonToFileMapping = buttonToFileMapping;
    }

    public Set<Integer> getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(Set<Integer> correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    public int getCardsTurnedGuessedRightInTotal() {
        return cardsTurnedGuessedRightInTotal;
    }

    public void setCardsTurnedGuessedRightInTotal(int cardsTurnedGuessedRightInTotal) {
        this.cardsTurnedGuessedRightInTotal = cardsTurnedGuessedRightInTotal;
    }

    public File getClosedFile() {
        return closedFile;
    }

    public void setClosedFile(File closedFile) {
        this.closedFile = closedFile;
    }

    public Button getPressedButton() {
        return pressedButton;
    }

    public void setPressedButton(Button pressedButton) {
        this.pressedButton = pressedButton;
    }
}
