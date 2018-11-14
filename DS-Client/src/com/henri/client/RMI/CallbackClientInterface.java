package com.henri.client.RMI;

public interface CallbackClientInterface {

    String notifyMe(String message)
            throws java.rmi.RemoteException;
}
