package com.henri.client.GUI.GameScreen4X4;

import com.henri.client.GUI.MainClient;
import com.henri.client.RMI.CallbackClientImpl;
import com.henri.client.RMI.CallbackClientInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameScreen4X4Controller implements Initializable {

    private int controllerId;

    private boolean viewOnly = false;
    private int gameId, numberOfUsers, gameTheme, maxUserAllowed;
    private boolean join = false;
    private ArrayList<String> gamePositions;
    private boolean myTurn = false;
    private String gameName;
    private ArrayList<Button> buttons;
    private ArrayList<String> images;
    private Map<Integer, String> buttonToFileMapping = new HashMap<>();
    private Button pressedButton = null;
    private Set<Integer> correctGuesses = new HashSet<>();
    private int cardsTurnedGuessedRightInTotal;
    private File closedFile;

    private CallbackClientInterface callBackObj;

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
            controllerId = rand.nextInt(100000);

            loadImages();

            addAllButtonsToList();

            tryJoin();

            requestGameConfig();





            gameNameLabel.setText(gameName);
            gameThemeLabel.setText(setGameThemeText(gameTheme));


            closedFile = new File("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\src\\com\\henri\\client\\GUI\\GameScreen4X4\\closed.png");

            mapButtonsToImages();

            updateGameField();

            checkTurn();


            if (cardsTurnedGuessedRightInTotal == 16) {
                notYourTurnLabel.setText("Game Over!");
                for(Button b : buttons){
                    b.setDisable(true);
                }
            }


            //Check if at least two users are registerd
            if (numberOfUsers < 2) {
                for (Button b : buttons) {
                    b.setDisable(true);
                }
                notYourTurnLabel.setText("Not enough players! Need at least 2!");
            }

            //Register callback for live updates
            registerCallback();


            //Set new on close handler to remove callback from app server
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                try {
                    MainClient.impl.removeCallback(controllerId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            });

        });


    }



    public void updateGameField() {

        for (int i = 0; i < gamePositions.size() - 1; i++) {
            int buttonNumber = Integer.parseInt(gamePositions.get(i)) - 1;
            Button b = buttons.get(buttonNumber);

            b.setDisable(false);
            //increase integer, so now we get true or false indicating if the image has been matched by another player
            i++;


            if (gamePositions.get(i).equals("false")) {
                Image image = new Image(closedFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(90);
                //b.setStyle("-fx-background-color: #FFFFFF");

                b.setGraphic(imageView);
            } else if (gamePositions.get(i).equals("true")) {
                File f2 = new File(buttonToFileMapping.get(buttonNumber));
                Image image = new Image(f2.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(90);
                b.setStyle("-fx-background-color: #FFFFFF");
                b.setGraphic(imageView);

                cardsTurnedGuessedRightInTotal++;
                //b.setDisable(true);
            }
        }

    }

    public void updateButton(int buttonId) {
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

    public void buttonClicked(ActionEvent actionEvent) throws RemoteException, InterruptedException {

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
                MainClient.impl.updateCardFlip(Integer.parseInt(b.getId()), gameId, controllerId);
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
                    MainClient.impl.updateGame(gameId, MainClient.username, gamePositions, correctGuesses.size() / 2);
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

                MainClient.impl.updateGame(gameId, MainClient.username, gamePositions, correctGuesses.size() / 2);

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

    public void refreshScreen(){
        Platform.runLater(() -> {
            pressedButton = null;
            cardsTurnedGuessedRightInTotal = 0;
            System.out.println("refreshing screen..");
            for(Button b : buttons){
                b.setStyle(null);
            }
            requestGameConfig();

            updateGameField();

            checkTurn();

            if (cardsTurnedGuessedRightInTotal == 16) {
                notYourTurnLabel.setText("Game Over!");
            }
            System.out.println("screen refreshed");
        });

    }

    public void loadImages(){
        images = new ArrayList<>();
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\after_effects_logo.PNG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\android_logo.JPG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\android_studio.JPG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\apple_logo.JPG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\chrome_logo.JPG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\IntelliJ_IDEA_Logo.JPG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\lightroom_logo.PNG");
        images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\photoshop_logo.PNG");
    }

    public void addAllButtonsToList(){
        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);
        buttons.add(button10);
        buttons.add(button11);
        buttons.add(button12);
        buttons.add(button13);
        buttons.add(button14);
        buttons.add(button15);
        buttons.add(button16);
    }

    public void tryJoin(){
        if (join) {
            try {
                MainClient.impl.requestJoin(gameId, MainClient.username);


            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    public void requestGameConfig() {
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

    public void checkTurn(){
        try {
            myTurn = MainClient.impl.checkTurn(gameId, MainClient.username);
            if (!myTurn) {
                //set all buttons on-clickable and set label "it's not your turn"
                for (Button b : buttons) {
                    b.setDisable(true);
                }
                notYourTurnLabel.setText("It's not your turn!");
                notYourTurnLabel.setVisible(true);
            }else{
                notYourTurnLabel.setVisible(false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void mapButtonsToImages(){
        //Map each Button to an image
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

    public void registerCallback(){
        try {
            callBackObj = new CallbackClientImpl(this);

            MainClient.impl.registerForCallback(controllerId, callBackObj, gameId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader gameDashboardScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
        Parent gameDashboardScreenPane = gameDashboardScreenLoader.load();
        Scene gameDashboardScreenScene = new Scene(gameDashboardScreenPane);

        MainClient.impl.removeCallback(controllerId);

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(gameDashboardScreenScene);
    }

    public String setGameThemeText(int gameTheme) {
        if (gameTheme == 1) {
            return "Software - theme";
        }
        return "Second - theme";
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

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }


}
