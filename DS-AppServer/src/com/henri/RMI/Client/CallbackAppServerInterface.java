package com.henri.RMI.Client;

public interface CallbackAppServerInterface {

    String notifyMe(String message)
            throws java.rmi.RemoteException;
}
