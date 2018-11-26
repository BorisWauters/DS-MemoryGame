package com.henri.RMI.Client;


/**
 * Interface Class which enables the Database Server to contact the App Server.
 * */
public interface CallbackAppServerInterface {

    /**
     * Function which notifies the client wth a given message.
     * @param message The message that has been sent from the Database Server
     * */
    String notifyMe(String message)
            throws java.rmi.RemoteException;
}
