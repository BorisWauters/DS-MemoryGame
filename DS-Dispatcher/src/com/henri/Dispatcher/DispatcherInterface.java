package com.henri.Dispatcher;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface which lets clients interact with the dispatcher
 * */
public interface DispatcherInterface extends Remote {

    /**
     * Function which lets the client setup at the dispatcher and returns the app port and the name of the app server registry
     * @param clientId The id of the client which requested the setup
     * @return ArrayList with the app port and the app server registry name
     * */
    ArrayList<Integer> setup(int clientId) throws RemoteException, NotBoundException;

    /**
     * Function which removes the client from the dispatcher
     * @param clientId The id of the client which wants to be removed
     * */
    void remove(int clientId) throws RemoteException;

    /**
     * Function which lets the user move app servers based on a given port number
     * @param clientId The id of the client which wants to move
     * @param portToMove  The port number to which the client wants to move
     * @return String The name of the app server registry on the given app port
     * */
    String requestMove(int clientId, int portToMove) throws RemoteException;
}
