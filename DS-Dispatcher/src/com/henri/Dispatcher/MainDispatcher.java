package com.henri.Dispatcher;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class which starts the dispatcher
 * */
public class MainDispatcher {

    public static void main(String... args) {
        try {
            //create on port 1103
            Registry registry = LocateRegistry.createRegistry(1103);

            //create a new service

            registry.rebind("DispatcherImplService", new DispatcherImpl());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }
}
