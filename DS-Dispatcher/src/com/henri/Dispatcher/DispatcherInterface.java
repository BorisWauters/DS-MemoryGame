package com.henri.Dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatcherInterface extends Remote {

    int setup(int clientId) throws RemoteException;

    void remove(int clientId) throws RemoteException;
}
