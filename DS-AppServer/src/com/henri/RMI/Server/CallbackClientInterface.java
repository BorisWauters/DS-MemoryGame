package com.henri.RMI.Server;

public interface CallbackClientInterface {

    //Needs to be the same as on the DSProject Client!

    /**
     * Function which enables the server to notify the client
     * */
    String notifyMe(String message)
            throws java.rmi.RemoteException;
}
