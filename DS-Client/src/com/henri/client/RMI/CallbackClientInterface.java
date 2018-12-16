package com.henri.client.RMI;

import java.rmi.RemoteException;

/**
 * Interface for callbacks performed on the client
 * */
public interface CallbackClientInterface extends java.rmi.Remote {

    /**
     * Function which propagates the updated card id as well sas the controller type for casting purposes client side
     * @param buttonId The id of the button which card was flipped
     * @param controllerType The type of the controller on which the game is running
     * */
    void updateCardFlip(int buttonId, int controllerType) throws RemoteException;

    /**
     * Function which refreshes the screen client side with a given controller type for casting purposes
     * @param controllerType The type of the controller on which the game is running
     * */
    void refreshScreen(int controllerType) throws RemoteException;

    void sendMessage(String message, String username, int controllerType) throws  RemoteException;
}
