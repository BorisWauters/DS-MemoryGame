package com.henri.client.cache;

import java.util.ArrayList;

/**
 * Class which contains the game information
 * */
public class Game {

    private int gameId;
    private String gamePositions;
    private int activeUsers;
    private int gameSize;
    private int userCreatedId;
    private int gameTheme;
    private int gameMaxPlayers;
    private String gameName;
    private int gameUsers;
    private int userIdOne, userIdTwo, userIdThree, userIdFour;
    private int userIdTurn;
    private int userOneScore, userTwoScore, userThreeScore, userFourScore;
    private int activeServer;

    public Game(int gameId, String gamePositions, int activeUsers, int gameSize, int userCreatedId, int gameTheme,
                int gameMaxPlayers, String gameName, int gameUsers, int userIdOne, int userIdTwo,
                int userIdThree, int userIdFour, int userIdTurn, int userOneScore, int userTwoScore,
                int userThreeScore, int userFourScore, int activeServer) {
        this.gameId = gameId;
        this.gamePositions = gamePositions;
        this.activeUsers = activeUsers;
        this.gameSize = gameSize;
        this.userCreatedId = userCreatedId;
        this.gameTheme = gameTheme;
        this.gameMaxPlayers = gameMaxPlayers;
        this.gameName = gameName;
        this.gameUsers = gameUsers;
        this.userIdOne = userIdOne;
        this.userIdTwo = userIdTwo;
        this.userIdThree = userIdThree;
        this.userIdFour = userIdFour;
        this.userIdTurn = userIdTurn;
        this.userOneScore = userOneScore;
        this.userTwoScore = userTwoScore;
        this.userThreeScore = userThreeScore;
        this.userFourScore = userFourScore;
        this.activeServer = activeServer;
    }

    public Game(ArrayList<String> gameConfig){
        this.gameId = Integer.parseInt(gameConfig.get(0));
        this.gamePositions = gameConfig.get(1);
        this.activeUsers = Integer.parseInt(gameConfig.get(2));
        this.gameSize = Integer.parseInt(gameConfig.get(3));
        this.userCreatedId = Integer.parseInt(gameConfig.get(4));
        this.gameTheme = Integer.parseInt(gameConfig.get(5));
        this.gameMaxPlayers = Integer.parseInt(gameConfig.get(6));
        this.gameName = gameConfig.get(7);
        this.gameUsers = Integer.parseInt(gameConfig.get(8));
        this.userIdOne = Integer.parseInt(gameConfig.get(9));
        this.userIdTwo = Integer.parseInt(gameConfig.get(10));
        this.userIdThree = Integer.parseInt(gameConfig.get(11));
        this.userIdFour = Integer.parseInt(gameConfig.get(12));
        this.userIdTurn = Integer.parseInt(gameConfig.get(13));
        this.userOneScore = Integer.parseInt(gameConfig.get(14));
        this.userTwoScore = Integer.parseInt(gameConfig.get(15));
        this.userThreeScore = Integer.parseInt(gameConfig.get(16));
        this.userFourScore = Integer.parseInt(gameConfig.get(17));
        this.activeServer = Integer.parseInt(gameConfig.get(18));

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGamePositions() {
        return gamePositions;
    }

    public void setGamePositions(String gamePositions) {
        this.gamePositions = gamePositions;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public int getGameTheme() {
        return gameTheme;
    }

    public void setGameTheme(int gameTheme) {
        this.gameTheme = gameTheme;
    }

    public int getGameMaxPlayers() {
        return gameMaxPlayers;
    }

    public void setGameMaxPlayers(int gameMaxPlayers) {
        this.gameMaxPlayers = gameMaxPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameUsers() {
        return gameUsers;
    }

    public void setGameUsers(int gameUsers) {
        this.gameUsers = gameUsers;
    }

    public int getUserIdOne() {
        return userIdOne;
    }

    public void setUserIdOne(int userIdOne) {
        this.userIdOne = userIdOne;
    }

    public int getUserIdTwo() {
        return userIdTwo;
    }

    public void setUserIdTwo(int userIdTwo) {
        this.userIdTwo = userIdTwo;
    }

    public int getUserIdThree() {
        return userIdThree;
    }

    public void setUserIdThree(int userIdThree) {
        this.userIdThree = userIdThree;
    }

    public int getUserIdFour() {
        return userIdFour;
    }

    public void setUserIdFour(int userIdFour) {
        this.userIdFour = userIdFour;
    }

    public int getUserIdTurn() {
        return userIdTurn;
    }

    public void setUserIdTurn(int userIdTurn) {
        this.userIdTurn = userIdTurn;
    }

    public int getUserOneScore() {
        return userOneScore;
    }

    public void setUserOneScore(int userOneScore) {
        this.userOneScore = userOneScore;
    }

    public int getUserTwoScore() {
        return userTwoScore;
    }

    public void setUserTwoScore(int userTwoScore) {
        this.userTwoScore = userTwoScore;
    }

    public int getUserThreeScore() {
        return userThreeScore;
    }

    public void setUserThreeScore(int userThreeScore) {
        this.userThreeScore = userThreeScore;
    }

    public int getUserFourScore() {
        return userFourScore;
    }

    public void setUserFourScore(int userFourScore) {
        this.userFourScore = userFourScore;
    }

    public int getActiveServer() {
        return activeServer;
    }

    public void setActiveServer(int activeServer) {
        this.activeServer = activeServer;
    }

    public int getUserCreatedId() {
        return userCreatedId;
    }

    public void setUserCreatedId(int userCreatedId) {
        this.userCreatedId = userCreatedId;
    }
}

