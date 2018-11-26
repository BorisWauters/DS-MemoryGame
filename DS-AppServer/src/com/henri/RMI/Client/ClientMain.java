package com.henri.RMI.Client;

import com.henri.server.InterfaceServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class implements the locating and lookup of the necessary registries.
 * */
public class ClientMain {

    public static InterfaceServer impl;

    /**
     * Function which locates the registry and binds it to the App Server enabling RMI.
     * */
    public void startClient(int port) {
        try {
            // fire to localhost on given port
            Registry registryServer = LocateRegistry.getRegistry("localhost", port);

            // search interfaceServer
            impl = (InterfaceServer) registryServer.lookup("ServerImplService");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
