package com.henri.Client;

import com.henri.server.InterfaceServerDS1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class implements the locating and lookup of the necessary registries.
 */
public class ClientMain {

    public final InterfaceServerDS1 impl;
    public final Registry registryServer;
    public final int databasePort;

    public ClientMain(int databasePort) throws RemoteException, NotBoundException {
        this.databasePort = databasePort;

        // fire to localhost on given port
        registryServer = LocateRegistry.getRegistry("localhost", databasePort);

        // search interfaceServer
        impl = (InterfaceServerDS1) registryServer.lookup("ServerImplService");


        System.out.println("CHECK 2: database port:" + databasePort);


    }


    public InterfaceServerDS1 getImpl() {
        return impl;
    }
}
