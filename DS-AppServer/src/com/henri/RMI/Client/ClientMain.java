package com.henri.RMI.Client;

import com.henri.server.InterfaceServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientMain {

    public static InterfaceServer impl;

    public void startClient() {
        try {
            // fire to localhost on port 1099
            Registry registryServer = LocateRegistry.getRegistry("localhost", 1102);

            // search interfaceServer

            impl = (InterfaceServer) registryServer.lookup("ServerImplService");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
