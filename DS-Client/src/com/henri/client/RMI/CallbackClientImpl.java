package com.henri.client.RMI;

import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

public class CallbackClientImpl extends UnicastRemoteObject implements  CallbackClientInterface{



    private GameScreen4X4Controller gameScreen4X4Controller;

    public CallbackClientImpl(GameScreen4X4Controller gameScreen4X4Controller) throws RemoteException {
        super( );
        this.gameScreen4X4Controller = gameScreen4X4Controller;
    }

    // function to notify the client from the server using the callback object at the server
    public String notifyMe(String message){
        String returnMessage = ">>>" + message;
        System.out.println(returnMessage);
        System.out.println("Enter your message:");
        return returnMessage;
    }

    @Override
    public void updateCardFlip(int buttonId) throws RemoteException{
        System.out.println("other user flipped a card: " + buttonId);
        gameScreen4X4Controller.updateButton(buttonId);
    }

    @Override
    public void refreshScreen() throws  RemoteException{
        gameScreen4X4Controller.refreshScreen();
    }

}
