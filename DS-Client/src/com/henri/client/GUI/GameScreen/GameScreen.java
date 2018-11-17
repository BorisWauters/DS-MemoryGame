package com.henri.client.GUI.GameScreen;

import com.henri.client.GUI.MainClient;
import com.henri.client.RMI.CallbackClientImpl;
import com.henri.client.RMI.CallbackClientInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameScreen {

    //Algemene klasse waar de controllers kunnen van overerven

    private int controllerId, controllerType;
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

    private CallbackClientInterface callbackObj;

    public void removeCallbackGeneral(Stage stage, int controllerId) {

            try {
                MainClient.impl.removeCallback(controllerId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);

    }

    public void updateButtonGeneral(int buttonId) {
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

    public void buttonClickedGeneral(ActionEvent actionEvent, Label notYourTurnLabel) throws InterruptedException, RemoteException {
        Button b = (Button) actionEvent.getSource();
        int buttonId1 = Integer.parseInt(b.getId());
        String fileLocation = buttonToFileMapping.get(buttonId1);
        File file1 = new File(fileLocation);

        Image image = new Image(file1.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(90);
        //b.setStyle("-fx-background-color: #FFFFFF");
        b.setGraphic(imageView);
        b.setDisable(true);
        //TimeUnit.SECONDS.sleep(1);
        //set turned image on "true" and propagate to other users
        for (int i = 0; i < gamePositions.size(); i++) {

            if (Integer.parseInt(b.getId()) == Integer.parseInt(gamePositions.get(i)) - 1) {
                i++;
                MainClient.impl.updateCardFlip(Integer.parseInt(b.getId()), gameId, controllerId, controllerType);
            } else {
                i++;
            }
        }


        if (pressedButton == null) {
            pressedButton = b;

        } else {
            for (Button button : buttons) {
                button.setDisable(true);
            }
            TimeUnit.MILLISECONDS.sleep(500);
            //check whether the images are the same

            int buttonId2 = Integer.parseInt(pressedButton.getId());
            String fileLocation2 = buttonToFileMapping.get(buttonId2);
            if (fileLocation.equals(fileLocation2)) {
                System.out.println("Correct");
                correctGuesses.add(buttonId1);
                correctGuesses.add(buttonId2);
                cardsTurnedGuessedRightInTotal++;
                cardsTurnedGuessedRightInTotal++;
                for (Button button : buttons) {
                    if (!correctGuesses.contains(Integer.parseInt(button.getId()))) {
                        button.setDisable(false);
                    }

                }
                b.setDisable(true);
                pressedButton.setDisable(true);
                pressedButton = null;

                if (cardsTurnedGuessedRightInTotal == 16) {
                    updateGamePositions();
                    MainClient.impl.updateGame(gameId, MainClient.username, gamePositions, correctGuesses.size() / 2, controllerType);
                    //MainClient.impl.requestGameWinner(gameId);
                    notYourTurnLabel.setText("Game Over!");
                }


            } else {
                System.out.println("Incorrect");
                notYourTurnLabel.setText("Incorrect! Not your turn anymore!");
                for (Button button : buttons) {
                    button.setDisable(true);
                }

                updateGamePositions();

                MainClient.impl.updateGame(gameId, MainClient.username, gamePositions, correctGuesses.size() / 2, controllerType);

            }


        }
    }

    public void updateGameFieldGeneral() {
        for (int i = 0; i < getGamePositions().size() - 1; i++) {
            int buttonNumber = Integer.parseInt(getGamePositions().get(i)) - 1;
            Button b = getButtons().get(buttonNumber);

            b.setDisable(false);
            //increase integer, so now we get true or false indicating if the image has been matched by another player
            i++;


            if (getGamePositions().get(i).equals("false")) {
                Image image = new Image(getClosedFile().toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(90);
                //b.setStyle("-fx-background-color: #FFFFFF");

                b.setGraphic(imageView);
            } else if (getGamePositions().get(i).equals("true")) {
                File f2 = new File(getButtonToFileMapping().get(buttonNumber));
                Image image = new Image(f2.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(90);
                b.setStyle("-fx-background-color: #FFFFFF");
                b.setGraphic(imageView);

                setCardsTurnedGuessedRightInTotal(getCardsTurnedGuessedRightInTotal() + 1);
                //b.setDisable(true);
            }
        }
    }

    public void updateGamePositions() {
        for (int i = 0; i < gamePositions.size(); i++) {

            if (correctGuesses.contains(Integer.parseInt(gamePositions.get(i)) - 1)) {
                i++;
                gamePositions.set(i, "true");
            } else {
                i++;
            }
        }
    }

    public void refreshScreenGeneral(Label notYourTurnLabel) {
        pressedButton = null;
        cardsTurnedGuessedRightInTotal = 0;
        System.out.println("refreshing screen..");
        for (Button b : buttons) {
            b.setStyle(null);
        }
        requestGameConfigGeneral();

        updateGameFieldGeneral();

        checkTurnGeneral(notYourTurnLabel);

        if (cardsTurnedGuessedRightInTotal == 16) {
            notYourTurnLabel.setText("Game Over!");
        }
        System.out.println("screen refreshed");
    }

    public void tryJoinGeneral() {
        if (join) {
            try {
                MainClient.impl.requestJoin(gameId, MainClient.username);


            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    public void requestGameConfigGeneral() {
        try {
            ArrayList<String> gameConfig = MainClient.impl.requestGameConfig(gameId);

            //extract info from array
            int i = 1;
            gameName = gameConfig.get(i);
            i++;
            numberOfUsers = Integer.parseInt(gameConfig.get(i));
            i++;
            maxUserAllowed = Integer.parseInt(gameConfig.get(i));
            i++;
            gameTheme = Integer.parseInt(gameConfig.get(i));
            i++;
            String gamePositionString = gameConfig.get(i);
            gamePositions = new ArrayList<>(Arrays.asList(gamePositionString.split("\\s*,\\s*")));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void checkTurnGeneral(Label notYourTurnLabel) {
        try {
            myTurn = MainClient.impl.checkTurn(gameId, MainClient.username);
            if (!myTurn) {
                //set all buttons on-clickable and set label "it's not your turn"
                for (Button b : buttons) {
                    b.setDisable(true);
                }
                notYourTurnLabel.setText("It's not your turn!");
                notYourTurnLabel.setVisible(true);
            } else {
                notYourTurnLabel.setVisible(false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void mapButtonsToImagesGeneral() {
        int j = 0;
        for (int i = 0; i < gamePositions.size() - 1; i++) {
            int buttonId1 = Integer.parseInt(gamePositions.get(i)) - 1;
            buttons.get(buttonId1).setId(String.valueOf(buttonId1));
            buttonToFileMapping.put(buttonId1, images.get(j));
            i++;
            i++;
            int buttonId2 = Integer.parseInt(gamePositions.get(i)) - 1;
            buttons.get(buttonId2).setId(String.valueOf(buttonId2));
            buttonToFileMapping.put(buttonId2, images.get(j));
            i++;
            j++;
        }
    }

    public void goBackGeneral(ActionEvent actionEvent) throws IOException {

        FXMLLoader gameDashboardScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
        Parent gameDashboardScreenPane = gameDashboardScreenLoader.load();
        Scene gameDashboardScreenScene = new Scene(gameDashboardScreenPane);

        MainClient.impl.removeCallback(getControllerId());

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScreenScene);

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

    public CallbackClientInterface getCallbackObj() {
        return callbackObj;
    }

    public void setCallbackObj(CallbackClientImpl callbackObj) {
        this.callbackObj = callbackObj;
    }

    public int getControllerType() {
        return controllerType;
    }

    public void setControllerType(int controllerType) {
        this.controllerType = controllerType;
    }
}
