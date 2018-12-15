package com.henri.Server;



import com.henri.server.InterfaceServerDS1;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class which initializes the registry creation and binding
 */
public class ServerMain {

    private final Registry registry;
    private final String serviceName;
    private final int appPort;
    private final int databasePort;
    private final InterfaceServerDS1 impl;

    public ServerMain(int appPort, String serviceName, int databasePort, InterfaceServerDS1 impl) throws RemoteException {
        this.appPort = appPort;
        this.serviceName = serviceName;
        this.databasePort = databasePort;
        this.impl = impl;
        //create on given port
        registry = LocateRegistry.createRegistry(appPort);

        //create a new service named CounterService

        registry.rebind(serviceName, new AppServerImpl(appPort, databasePort, impl));


        System.out.println("System is ready");
    }



    public void shutdown() throws RemoteException, NotBoundException {
        registry.unbind(serviceName);
        UnicastRemoteObject.unexportObject(registry, true);
    }


}
