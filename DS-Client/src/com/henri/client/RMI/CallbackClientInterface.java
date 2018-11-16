package com.henri.client.RMI;

import java.rmi.RemoteException;

public interface CallbackClientInterface extends java.rmi.Remote{

    String notifyMe(String message)
            throws RemoteException;

    void updateCardFlip(int buttonId, int controllerType) throws RemoteException;

    void refreshScreen(int controllerType) throws RemoteException;
}
