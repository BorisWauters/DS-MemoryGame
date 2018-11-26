package com.henri.RMI.Client;

import java.rmi.RemoteException;


/**
 * This class is the implementation of the @CallbackAppServerInterface class.
 * @see com.henri.RMI.Client.CallbackAppServerInterface
 * */
public class CallbackAppServerImpl implements CallbackAppServerInterface {


    public CallbackAppServerImpl() throws RemoteException {
        super( );
    }

    /**
     * function to notify the client from the server using the callback object at the server.
     * @return String
     * @param message The message which contains the data sent to the AppServer from the Database Server
     * */
    public String notifyMe(String message){
        String returnMessage = ">>>" + message;
        System.out.println(returnMessage);
        System.out.println("Enter your message:");
        return returnMessage;
    }
}
