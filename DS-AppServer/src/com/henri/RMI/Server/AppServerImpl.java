package com.henri.RMI.Server;


import com.henri.client.RMI.CallbackClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static com.henri.RMI.Client.ClientMain.impl;

public class AppServerImpl extends UnicastRemoteObject implements AppServerInterface {

    private Set<String> userSet = new HashSet<>();
    private Map<Integer, ArrayList<Object>> clientList;

    public AppServerImpl() throws RemoteException {
        clientList = new HashMap<>();
    }

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

    @Override
    public void removeCallback(int controllerId) throws RemoteException {
        int keyToRemove = -1;
        for (Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()) {
            if (entry.getKey() == controllerId) keyToRemove = entry.getKey();
        }
        clientList.remove(keyToRemove);
    }

    @Override
    public String setupMessage(String username, String password) throws RemoteException {
        String response = impl.setupMessage(username, password);
        if (response.equals("true")) {
            return "true";
        } else {
            return "false";
        }

    }

    @Override
    public boolean checkUsername(String username) throws RemoteException {
        return impl.checkUsername(username);
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException {
        impl.registerUser(username, password);
    }

    @Override
    public String acquireSessionId(String username) throws RemoteException {
        return impl.acquireSessionId(username);
    }

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

    @Override
    public void updateGame(int gameId, String username, ArrayList<String> gamePositions, int score, int controllerType) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gamePositions.size(); i++) {
            sb.append(gamePositions.get(i));
            if (i < gamePositions.size() - 1) {
                sb.append(",");
            }
        }
        boolean updated = impl.updateGame(gameId, username, sb.toString(), score);
        if (updated) {
            for (Map.Entry<Integer, ArrayList<Object>> entry : clientList.entrySet()) {
                int gameIdFromList = (int) entry.getValue().get(0);
                if (gameIdFromList == gameId) {
                    CallbackClientInterface callbackClientObject = (CallbackClientInterface) entry.getValue().get(1);
                    callbackClientObject.refreshScreen(controllerType);
                }
            }
        }


    }

    @Override
    public ArrayList<String> requestGames(String username) throws RemoteException {
        return impl.requestGames(username);
    }

    @Override
    public ArrayList<String> requestAllGames(String username) throws RemoteException {
        return impl.requestAllGames(username);
    }

    @Override
    public void requestJoin(int gameId, String username) throws RemoteException {
        impl.requestJoin(gameId, username);
    }

    @Override
    public boolean checkTurn(int gameId, String username) throws RemoteException {
        return impl.checkTurn(gameId, username);
    }

    @Override
    public ArrayList<String> requestGameConfig(int gameId) throws RemoteException {
        return impl.requestGameConfig(gameId);
    }

    @Override
    public ArrayList<String> requestGameWinner(int gameId) throws RemoteException {
        return impl.requestGameWinner(gameId);
    }

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

    @Override
    public boolean checkSessionIdentifier(int sessionId, String sessionIdentifier) throws RemoteException {
        return impl.checkSessionIdentifier(sessionId, sessionIdentifier);
    }

    @Override
    public ArrayList<String> requestTopPlayers() throws RemoteException{
        return impl.requestTopPlayers();
    }
}
