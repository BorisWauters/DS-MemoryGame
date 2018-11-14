package com.henri.client.RMI;

import java.rmi.RemoteException;

public class CallbackClientImpl implements  CallbackClientInterface{

    public CallbackClientImpl() throws RemoteException {
        super( );
    }

    // function to notify the client from the server using the callback object at the server
    public String notifyMe(String message){
        String returnMessage = ">>>" + message;
        System.out.println(returnMessage);
        System.out.println("Enter your message:");
        return returnMessage;
    }
}
