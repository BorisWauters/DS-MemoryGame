package com.henri.client.RMI;

import com.henri.client.GUI.GameScreen.GameScreen;
import com.henri.client.GUI.GameScreen4X4.GameScreen4X4Controller;
import com.henri.client.GUI.GameScreen4X6.GameScreen4X6Controller;
import com.henri.client.GUI.GameScreen6X6.GameScreen6X6Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class with the implementation of the Callback Client interface
 * */
public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {


    private Object controller;


    public CallbackClientImpl(GameScreen4X4Controller gameScreen4X4Controller) throws RemoteException {
        super();
        controller = gameScreen4X4Controller;
    }

    public CallbackClientImpl(GameScreen4X6Controller gameScreen4X6Controller) throws RemoteException {
        super();
        controller = gameScreen4X6Controller;
    }

    public CallbackClientImpl(GameScreen6X6Controller gameScreen6X6Controller) throws RemoteException {
        super();
        controller = gameScreen6X6Controller;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void updateCardFlip(int buttonId, int controllerType) throws RemoteException {
        System.out.println("other user flipped a card: " + buttonId);
        if (controllerType == 1) {
            GameScreen4X4Controller controller4X4 = (GameScreen4X4Controller) controller;
            controller4X4.updateButton(buttonId);
        } else if (controllerType == 2) {
            GameScreen6X6Controller controller6X6 = (GameScreen6X6Controller) controller;
            controller6X6.updateButton(buttonId);
        } else if (controllerType == 3) {
            GameScreen4X6Controller controller4X6 = (GameScreen4X6Controller) controller;
            controller4X6.updateButton(buttonId);
        }

    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void refreshScreen(int controllerType) throws RemoteException {
        if (controllerType == 1) {
            GameScreen4X4Controller controller4X4 = (GameScreen4X4Controller) controller;
            controller4X4.refreshScreen();
        } else if (controllerType == 2) {
            GameScreen6X6Controller controller6X6 = (GameScreen6X6Controller) controller;
            controller6X6.refreshScreen();
        } else if (controllerType == 3) {
            GameScreen4X6Controller controller4X6 = (GameScreen4X6Controller) controller;
            controller4X6.refreshScreen();
        }

    }

    @Override
    public void sendMessage(String message, String username, int controllerType){

        if (controllerType == 1) {
            GameScreen4X4Controller controller4X4 = (GameScreen4X4Controller) controller;
            controller4X4.updateChat(message, username);
        } else if (controllerType == 2) {
            GameScreen6X6Controller controller6X6 = (GameScreen6X6Controller) controller;
            controller6X6.updateChat(message, username);
        } else if (controllerType == 3) {
            GameScreen4X6Controller controller4X6 = (GameScreen4X6Controller) controller;
            controller4X6.updateChat(message, username);
        }
    }

}
