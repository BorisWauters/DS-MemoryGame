package com.henri.RMI.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public void startServer() {
        try {
            //create on ort 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            //create a new service named CounterService

            registry.rebind("AppServerImplService", new AppServerImpl());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }


}
