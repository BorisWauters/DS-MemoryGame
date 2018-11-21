package com.henri.RMI.Server;

import com.henri.client.RMI.CallbackClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AppServerInterface extends Remote {

    void registerForCallback(int controllerId,
                             CallbackClientInterface callbackClientObject, int gameId
    ) throws RemoteException;

    void removeCallback(int controllerId) throws RemoteException;

    String setupMessage(String username, String password) throws RemoteException;

    boolean checkUsername(String username) throws RemoteException;

    void registerUser(String username, String password) throws RemoteException;

    String acquireSessionId(String username) throws RemoteException;

    void createGame(String gameConfig, String username) throws RemoteException;

    ArrayList<String> requestGames(String username) throws RemoteException;

    ArrayList<String> requestAllGames(String username) throws RemoteException;


    void requestJoin(int gameId, String username) throws RemoteException;

    boolean checkTurn(int gameId, String username) throws RemoteException;

    ArrayList<String> requestGameConfig(int gameId) throws RemoteException;

    void updateGame(int gameId, String username, ArrayList<String> gamePositions, int score, int controllerType) throws RemoteException;

    ArrayList<String> requestGameWinner(int gameId) throws RemoteException;

    void updateCardFlip(int buttonId, int gameId, int controllerId, int controllerType) throws RemoteException;

    boolean checkSessionIdentifier(int sessionId, String sessionIdentifier) throws RemoteException;

    ArrayList<String> requestTopPlayers() throws RemoteException;
}
