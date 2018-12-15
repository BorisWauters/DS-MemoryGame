package com.henri;

import com.henri.Client.ClientMain;
import com.henri.Server.ServerMain;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * Class which is used to run the App Server
 */
public class MainAppServer {
    public final int appPort;
    public final int databasePort;

    private final ServerMain serverMain;
    private final ClientMain clientMain;
    private String serviceName;
    //public final int i = 0;

    public  MainAppServer(int databasePort, int appPort, String serviceName) throws RemoteException, NotBoundException {
        this.databasePort = databasePort;
        this.appPort = appPort;
        this.serviceName = serviceName;

        System.out.println("Enabling database connection on port: " + databasePort);
        clientMain = new ClientMain(databasePort);

        System.out.println("Starting AppServer on port: " + appPort);
        serverMain = new ServerMain(appPort, serviceName, databasePort, clientMain.getImpl());


    }



    public void shutDown() throws RemoteException, NotBoundException {
        serverMain.shutdown();

    }



}
