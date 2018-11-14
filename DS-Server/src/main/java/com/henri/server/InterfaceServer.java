package com.henri.server;

import com.henri.RMI.Client.CallbackAppServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceServer extends Remote {

    void registerForCallback(
            CallbackAppServerInterface callbackClientObject, String username
    ) throws RemoteException;

    String setupMessage(String username, String password) throws RemoteException;

    boolean checkUsername(String username) throws RemoteException;

    void registerUser(String username, String password) throws RemoteException;

    String acquireSessionId(String username) throws RemoteException;

    void createGame(ArrayList<String> gamePositions, String username) throws  RemoteException;

    ArrayList<String> requestGames(String username) throws RemoteException;

    ArrayList<String> requestAllGames(String username) throws RemoteException;

    void requestJoin(int gameId, String username) throws RemoteException;

    boolean checkTurn(int gameId, String username) throws  RemoteException;

    ArrayList<String> requestGameConfig(int gameId) throws RemoteException;

    void updateGame(int gameId, String username, String gamePositions, int score) throws  RemoteException;

    String requestGameWinner(int gameId) throws RemoteException;
}
