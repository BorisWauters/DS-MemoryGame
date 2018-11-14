package com.henri.client.GUI.GameScreen4X4;

import com.henri.client.GUI.MainClient;
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
import javafx.stage.Stage;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class GameScreen4X4Controller implements Initializable {


    private boolean viewOnly = false;
    private int gameId, numberOfUsers, gameTheme, maxUserAllowed;
    private boolean join = false;
    private ArrayList<String> gamePositions;
    private boolean myTurn = false;
    private String gameName;
    private ArrayList<Button> buttons;
    private ArrayList<String> images;
    private Map<Integer, String> buttonToFileMapping = new HashMap<>();
    Timer myTimer;
    private Button pressedButton = null;
    private Set<Integer> correctGuesses = new HashSet<>();
    int cardsTurnedGuessedRightInTotal;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button button10;

    @FXML
    private Button button11;

    @FXML
    private Button button12;

    @FXML
    private Button button13;

    @FXML
    private Button button14;

    @FXML
    private Button button15;

    @FXML
    private Button button16;

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
            //load all images
            images = new ArrayList<>();
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\after_effects_logo.PNG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\android_logo.JPG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\android_studio.JPG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\apple_logo.JPG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\chrome_logo.JPG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\IntelliJ_IDEA_Logo.JPG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\lightroom_logo.PNG");
            images.add("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\images-set1\\photoshop_logo.PNG");

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

            if (join) {
                try {
                    MainClient.impl.requestJoin(gameId, MainClient.username);


                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
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


            try {
                myTurn = MainClient.impl.checkTurn(gameId, MainClient.username);
                if (!myTurn) {
                    //set all buttons on-clickable and set label "it's not your turn"
                    for (Button b : buttons) {
                        b.setDisable(true);
                    }
                    notYourTurnLabel.setText("It's not your turn!");
                    notYourTurnLabel.setVisible(true);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            gameNameLabel.setText(gameName);
            gameThemeLabel.setText(setGameThemeText(gameTheme));


            File closedFile = new File("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\src\\com\\henri\\client\\GUI\\GameScreen4X4\\closed.png");


            //Map each Button to an image
            int j = 0;
            for (int i = 0; i < gamePositions.size() - 1; i++) {
                int buttonId = Integer.parseInt(gamePositions.get(i)) - 1;
                buttonToFileMapping.put(buttonId, images.get(j));
                i++;
                i++;
                int buttonId2 = Integer.parseInt(gamePositions.get(i)) - 1;
                buttonToFileMapping.put(buttonId2, images.get(j));
                i++;
                j++;
            }

            for (int i = 0; i < gamePositions.size() - 1; i++) {
                int buttonNumber = Integer.parseInt(gamePositions.get(i)) - 1;
                Button b = buttons.get(buttonNumber);
                b.setId(String.valueOf(buttonNumber));

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

            if(cardsTurnedGuessedRightInTotal == 16){
                notYourTurnLabel.setText("Game Over!");
            }
            myTimer = new Timer(1000, new TimerListener());

            //Check if at least two users are registerd
            if(numberOfUsers < 2){
                for(Button b : buttons){
                    b.setDisable(true);
                }
                notYourTurnLabel.setText("Not enough players! Need at least 2!");
            }
        });


    }

    private class TimerListener implements ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            myTimer.stop();
        }
    }


    public void buttonClicked(ActionEvent actionEvent) throws InterruptedException, RemoteException {
        File closedFile = new File("C:\\docs\\KUL\\MASTER\\semester1\\distributed systems\\Lab\\DSProject-Client\\src\\com\\henri\\client\\GUI\\GameScreen4X4\\closed.png");

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

        if (pressedButton == null) {
            pressedButton = b;

        } else {
            for (Button button : buttons) {
                button.setDisable(true);
            }
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

                if(cardsTurnedGuessedRightInTotal == 16 ){
                    updateGamePositions();
                    MainClient.impl.updateGame(gameId,MainClient.username,gamePositions, correctGuesses.size()/2);
                    //MainClient.impl.requestGameWinner(gameId);
                    notYourTurnLabel.setText("Game Over!");
                }


            } else {
                System.out.println("Incorrect");
                myTimer.start();
                notYourTurnLabel.setText("Incorrect! Not your turn anymore!");
                for (Button button : buttons) {
                    button.setDisable(true);
                }


                updateGamePositions();
                MainClient.impl.updateGame(gameId,MainClient.username,gamePositions, correctGuesses.size()/2);

            }


        }


    }

    public void updateGamePositions() {
        for (int i = 0; i < gamePositions.size(); i++) {

            if(correctGuesses.contains(Integer.parseInt(gamePositions.get(i))-1)){
                i++;
                gamePositions.set(i,"true");
            }else{
                i++;
            }
        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader gameDashboardScreenLoader = new FXMLLoader(getClass().getClassLoader().getResource("com/henri/client/GUI/GameDashboard/GameDashboardScreen.fxml"));
        Parent gameDashboardScreenPane = gameDashboardScreenLoader.load();
        Scene gameDashboardScreenScene = new Scene(gameDashboardScreenPane);

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
