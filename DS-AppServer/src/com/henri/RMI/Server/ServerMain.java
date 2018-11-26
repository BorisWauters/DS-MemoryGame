package com.henri.RMI.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class which initializes the registry creation and binding
 * */
public class ServerMain {

    /**
     * Function which creates the required registries and binds them
     * */
    public void startServer(int port) {
        try {
            //create on given port
            Registry registry = LocateRegistry.createRegistry(port);

            //create a new service named CounterService

            registry.rebind("AppServerImplService", new AppServerImpl());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }


}
