package com.henri.Server;


import com.henri.server.InterfaceServerDS1;
import com.henri.client.RMI.CallbackClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


/**
 * Class which is the implementation if the @AppServerInterface class and performs functions invoked by the client.
 */
public class AppServerImpl extends UnicastRemoteObject implements AppServerInterface {


    private Map<Integer, ArrayList<Object>> clientList;
    private final int appPort;
    private final int databasePort;
    private final InterfaceServerDS1 impl;
    private final Map<Integer, Game> activeGames = new HashMap<>();

    /**
     * {@inheritDoc}
     * */
    public AppServerImpl(int appPort, int databasePort, InterfaceServerDS1 impl) throws RemoteException {
        clientList = new HashMap<>();
        this.appPort = appPort;
        this.databasePort = databasePort;
        this.impl = impl;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public synchronized void registerForCallback(int controllerId,
                                                 CallbackClientInterface callbackClientObject, int gameId)
            throws RemoteException {
        // store the callback object into the Map
        ArrayList<Object> values = new ArrayList<>();
        values.add(gameId);
        values.add(callbackClientObject);
        clientList.put(controllerId, values);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void removeCallback(int controllerId) throws RemoteException {
        int keyToRemove = -1;
        for (Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()) {
            if (entry.getKey() == controllerId) keyToRemove = entry.getKey();
        }
        clientList.remove(keyToRemove);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public String setupMessage(String username, String password) throws RemoteException {
        String response = impl.setupMessage(username, password);
        if (response != "-1") {
            return response;
        } else {
            return "-1";
        }

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean checkUsername(String username) throws RemoteException {
        return impl.checkUsername(username);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void registerUser(String username, String password) throws RemoteException {
        impl.registerUser(username, password);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public String acquireSessionId(String username) throws RemoteException {
        return impl.acquireSessionId(username);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void createGame(String gameConfig, String username) throws RemoteException {
        ArrayList<String> gameConfigList = new ArrayList<>(Arrays.asList(gameConfig.split("\\s*,\\s*")));
        int numberOfPLayers = Integer.parseInt(gameConfigList.get(0));
        int gameSize = Integer.parseInt(gameConfigList.get(1));
        int gameTheme = Integer.parseInt(gameConfigList.get(2));
        String gameName = gameConfigList.get(3);

        int positionArraySize = 0;

        //create array with positions
        ArrayList<String> gamePositions = new ArrayList<>();
        if (gameSize == 1) {
            //4X4 game
            positionArraySize = 16;
        } else if (gameSize == 2) {
            //6X6 game
            positionArraySize = 36;
        } else if (gameSize == 3) {
            //4X6 game
            positionArraySize = 24;
        }

        //add integers to resemble icons
        for (int i = 1; i <= positionArraySize; i++) {
            gamePositions.add(String.valueOf(i));
        }

        //randomize the positions
        Random gen = new Random();
        for (int i = 0; i < gamePositions.size(); i++) {
            int rand = gen.nextInt(gamePositions.size());
            String temp = gamePositions.get(i);
            gamePositions.set(i, gamePositions.get(rand));
            gamePositions.set(rand, temp);
        }

        // add true or false, to show whether icons have been turned
        for (int i = 1; i < gamePositions.size() + 1; i++) {
            gamePositions.add(i, "false");
            i++;
        }

        //add position to show number of users
        gamePositions.add(0, String.valueOf(numberOfPLayers));

        //add position to show game theme
        gamePositions.add(1, String.valueOf(gameTheme));

        //add game name
        gamePositions.add(2, gameName);

        //propagate this game to the server to be saved
        impl.createGame(gamePositions, username);


    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void updateGame(int gameId, int userId, ArrayList<String> gamePositions, int score, int controllerType) throws RemoteException {
        boolean allCardsTurned = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gamePositions.size(); i++) {
            sb.append(gamePositions.get(i));
            if (gamePositions.get(i).equals("false")) {
                allCardsTurned = false;
            }
            if (i < gamePositions.size() - 1) {
                sb.append(",");
            }
        }

        Game game = activeGames.get(gameId);
        game.setGamePositions(sb.toString());
        setScoreAndNextPlayer(game, userId, score);

        //boolean updated = impl.updateGame(gameId, username, sb.toString(), score);

        for (Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()) {
            int gameIdFromList = (int) entry.getValue().get(0);
            if (gameIdFromList == gameId) {
                CallbackClientInterface callbackClientObject = (CallbackClientInterface) entry.getValue().get(1);
                callbackClientObject.refreshScreen(controllerType);
            }
        }

        if(allCardsTurned){
            impl.updateEntireGame(createGameConfig(game));
        }

    }

    /**
     * {@inheritDoc}
     * */
    public void setScoreAndNextPlayer(Game game, int userId, int score) {

        if (game.getUserIdOne() == userId) {
            game.setUserOneScore(game.getUserOneScore() + score);
            game.setUserIdTurn(game.getUserIdTwo());
        } else if (game.getUserIdTwo() == userId) {
            game.setUserTwoScore(game.getUserTwoScore() + score);
            if (game.getGameUsers() > 2) {
                game.setUserIdTurn(game.getUserIdThree());
            } else if (game.getGameUsers() == 2) {
                game.setUserIdTurn(game.getUserIdOne());
            }
        } else if (game.getUserIdThree() == userId) {
            game.setUserThreeScore(game.getUserThreeScore() + score);
            if (game.getGameUsers() > 3) {
                game.setUserIdTurn(game.getUserIdFour());
            } else if (game.getGameUsers() == 3) {
                game.setUserIdTurn(game.getUserIdOne());
            }
        } else if (game.getUserIdFour() == userId) {
            game.setUserFourScore(game.getUserFourScore() + score);
            game.setUserIdTurn(game.getUserIdOne());
        }

    }
    /**
     * {@inheritDoc}
     * */
    @Override
    public ArrayList<String> requestGames(String username) throws RemoteException {
        return impl.requestGames(username);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ArrayList<String> requestAllGames(String username) throws RemoteException {
        return impl.requestAllGames(username);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void requestJoin(int gameId, int userId) throws RemoteException {
        Game game = activeGames.get(gameId);
        int gameUsers = game.getGameUsers() + 1;
        game.setGameUsers(gameUsers);
        setPlayerNumber(game, userId);

        //impl.requestJoin(gameId, username);
    }

    /**
     * {@inheritDoc}
     * */
    public void setPlayerNumber(Game game, int userId) {
        if (game.getUserIdTwo() == 0) {

            game.setUserIdTwo(userId);


        } else if (game.getUserIdThree() == 0) {

            game.setUserIdThree(userId);

        } else if (game.getUserIdFour() == 0) {

            game.setUserIdFour(userId);

        }

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean checkTurn(int gameId, int userId) throws RemoteException {
        Game game = activeGames.get(gameId);
        if (game.getUserIdTurn() == userId) {
            return true;
        }

        return false;

        //return impl.checkTurn(gameId, username);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ArrayList<String> requestGameConfig(int gameId) throws RemoteException {
        Game game = activeGames.get(gameId);
        ArrayList<String> gameConfig = new ArrayList<>();
        gameConfig.add(String.valueOf(game.getGameId()));         //game ID
        gameConfig.add(game.getGameName());                       //Game name
        gameConfig.add(String.valueOf(game.getGameUsers()));      //number of users in the game
        gameConfig.add(String.valueOf(game.getGameMaxPlayers())); //game max players allowed
        gameConfig.add(String.valueOf(game.getGameTheme()));      //game theme
        gameConfig.add(game.getGamePositions());

        return gameConfig;

        //return impl.requestGameConfig(gameId);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ArrayList<String> requestGameWinner(int gameId) throws RemoteException {
        //
        return impl.requestGameWinner(gameId);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void updateCardFlip(int buttonId, int gameId, int controllerId, int controllerType) throws RemoteException {
        for (Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()) {
            int gameIdFromList = (int) entry.getValue().get(0);
            if (gameIdFromList == gameId && controllerId != entry.getKey()) {
                CallbackClientInterface callbackClientObject = (CallbackClientInterface) entry.getValue().get(1);
                callbackClientObject.updateCardFlip(buttonId, controllerType);
            }
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean checkSessionIdentifier(int sessionId, String sessionIdentifier) throws RemoteException {
        System.out.println("Using app port: " + appPort);
        System.out.println("And database port: " + databasePort);
        return impl.checkSessionIdentifier(sessionId, sessionIdentifier);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public ArrayList<String> requestTopPlayers() throws RemoteException {
        return impl.requestTopPlayers();
    }

    @Override
    public Game enterGame(int gameId) throws RemoteException {
        Game gameEntityDS1 = activeGames.get(gameId);
        gameEntityDS1.setActiveUsers(gameEntityDS1.getActiveUsers() + 1);
        return gameEntityDS1;
        //impl.enterGame(gameId);
        //gamesOnServer.add(gameId);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean checkGameAvailability(int gameId) throws RemoteException {
        if (activeGames.containsKey(gameId)) return true;
        else return false;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public int checkGameOnOtherServer(int gameId) throws RemoteException {
        int serverPort = impl.checkGameOnOtherServer(gameId);
        if (serverPort != -1) return serverPort;
        else return -1;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public Game enterNewGame(int gameId) throws RemoteException {
        ArrayList<String> gameConfig = impl.enterNewGame(gameId, appPort);
        Game game = new Game(gameConfig);
        game.setActiveUsers(game.getActiveUsers() + 1);
        activeGames.put(gameId, game);
        return game;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void leaveGame(int gameId) throws RemoteException {
        Game game = activeGames.get(gameId);
        int users = game.getActiveUsers();
        game.setActiveUsers(users - 1);
        if (game.getActiveUsers() == 0) {

            game.setActiveServer(0);
            impl.updateEntireGame(createGameConfig(game));
            activeGames.remove(gameId);
        }

        //impl.leaveGame(gameId);
    }

    @Override
    public void sendMessage(String message, String username, int gameId, int controllerType) throws RemoteException {
        for(Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()){
            if((int)entry.getValue().get(0) == gameId){
                CallbackClientInterface callbackClientObject = (CallbackClientInterface) entry.getValue().get(1);
                callbackClientObject.sendMessage(message, username, controllerType);
            }
        }
    }

    /**
     * Function which creates an list which contains the configuration of the game
     *
     * @param game The game which needs to be transformed
     * @return ArrayList The list containing the configuration of the given game as strings
     * */
    public ArrayList<String> createGameConfig(Game game) {
        ArrayList<String> gameConfig = new ArrayList<>();
        gameConfig.add(String.valueOf(game.getGameId()));
        gameConfig.add(game.getGamePositions());
        gameConfig.add(String.valueOf(game.getActiveUsers()));
        gameConfig.add(String.valueOf(game.getGameSize()));
        gameConfig.add(String.valueOf(game.getUserCreatedId()));
        gameConfig.add(String.valueOf(game.getGameTheme()));
        gameConfig.add(String.valueOf(game.getGameMaxPlayers()));
        gameConfig.add(game.getGameName());
        gameConfig.add(String.valueOf(game.getGameUsers()));
        gameConfig.add(String.valueOf(game.getUserIdOne()));
        gameConfig.add(String.valueOf(game.getUserIdTwo()));
        gameConfig.add(String.valueOf(game.getUserIdThree()));
        gameConfig.add(String.valueOf(game.getUserIdFour()));
        gameConfig.add(String.valueOf(game.getUserIdTurn()));
        gameConfig.add(String.valueOf(game.getUserOneScore()));
        gameConfig.add(String.valueOf(game.getUserTwoScore()));
        gameConfig.add(String.valueOf(game.getUserThreeScore()));
        gameConfig.add(String.valueOf(game.getUserFourScore()));
        gameConfig.add(String.valueOf(game.getActiveServer()));

        return gameConfig;
    }
}
