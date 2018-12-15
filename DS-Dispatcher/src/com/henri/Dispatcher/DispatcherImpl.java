package com.henri.Dispatcher;

import com.henri.MainAppServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which implements the dispatcher functionality as well as the dispatcher interface
 * */
public class DispatcherImpl extends UnicastRemoteObject implements DispatcherInterface {

    private Map<Integer, Integer> appServerPorts = new HashMap();
    private ArrayList<Integer> databaseServerPorts = new ArrayList<>();
    private Map<Integer, Integer> clientToAppServerMapping = new HashMap<>();
    public static Map<Integer, MainAppServer> activeAppServers = new HashMap<>();
    private Map<Integer, Integer> appServerToDatabaseMapping = new HashMap<>();
    private Map<Integer,Integer> appPortServiceNameMapping = new HashMap<>();

    public DispatcherImpl() throws RemoteException {
        appServerPorts.put(1104, 0);
        appServerPorts.put(1105, 0);
        appServerPorts.put(1106, 0);
        appServerPorts.put(1107, 0);
        appServerPorts.put(1108, 0);
        appServerPorts.put(1109, 0);
        appServerPorts.put(1110, 0);
        appServerPorts.put(1111, 0);
        appServerPorts.put(1112, 0);
        appServerPorts.put(1113, 0);

        databaseServerPorts.add(1114);
        databaseServerPorts.add(1115);
        databaseServerPorts.add(1116);
        System.out.println("lists initialized");
        InputThread inputThread = new InputThread();
        inputThread.start();

    }

    /**
     * {@inheritDoc}
     * */
    public ArrayList<Integer> setup(int clientId) throws RemoteException, NotBoundException {
        int i = 0;
        int databasePort = 0;
        for (Map.Entry<Integer, Integer> entry : appServerPorts.entrySet()) {
            if (entry.getValue() == 0) {
                clientToAppServerMapping.put(clientId, entry.getKey());

                //Map every other app server to another database server
                if (i % 3 == 0) {
                    databasePort = databaseServerPorts.get(0);
                } else if (i % 3 == 1) {
                    databasePort = databaseServerPorts.get(1);
                } else if (i % 3 == 2) {
                    databasePort = databaseServerPorts.get(2);
                }
                appServerToDatabaseMapping.put(entry.getKey(),databasePort);
               /* String[] args = new String[3];
                args[0] = String.valueOf(databasePort);
                args[1] = String.valueOf(entry.getKey());
                args[2] = String.valueOf(i);*/
                MainAppServer mainAppServer = new MainAppServer(databasePort, entry.getKey(), String.valueOf(i));
                appPortServiceNameMapping.put(entry.getKey(),i);

                entry.setValue(entry.getValue() + 1);
                activeAppServers.put(entry.getKey(),mainAppServer);
                System.out.println("Connected to Appserver on port: " + entry.getKey() + " and database server on port:" + databasePort);

                System.out.println("All active servers");
                for (Map.Entry<Integer, MainAppServer> entry2 : activeAppServers.entrySet()) {
                    System.out.println("port: " + entry2.getKey());
                }
                for(Map.Entry<Integer, Integer> entry3 : clientToAppServerMapping.entrySet()) System.out.println("Client: " + entry3.getKey() + " App server: " + entry3.getValue());

                ArrayList<Integer> res = new ArrayList<>();
                res.add(entry.getKey());
                res.add(i);
                return res;
            }/*else if(entry.getValue() < 20){    //should be more than 20, (per 20 active games we would start a new app server)
                clientToAppServerMapping.put(clientId, entry.getKey());
                return entry.getKey();
            }*/
            i++;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * */
    public void remove(int clientId) throws RemoteException {
        //decrease the number of active clients
        int port = 0;
        for (Map.Entry<Integer, Integer> entry : appServerPorts.entrySet()) {
            if (entry.getKey().equals(clientToAppServerMapping.get(clientId))) {
                entry.setValue(entry.getValue() - 1);
                port = entry.getKey();
            }
        }
        System.out.println("Removed client app server port: " + port);
        clientToAppServerMapping.remove(clientId);

        if(appServerPorts.get(port) == 0){
            killAppServerIfEmpty(port);
        }

    }

    /**
     * {@inheritDoc}
     * */
    public String requestMove(int clientId, int portToMove) throws RemoteException{
        int currentPort = clientToAppServerMapping.get(clientId);
        //decrease the number of active users on this server by one
        appServerPorts.replace(currentPort, appServerPorts.get(currentPort) - 1);
        //move the client
        clientToAppServerMapping.replace(clientId,portToMove);
        //increase the number of users
        appServerPorts.replace(portToMove,appServerPorts.get(portToMove) + 1);
        System.out.println("new port: " + clientToAppServerMapping.get(clientId) + " clients: " + appServerPorts.get(portToMove));
        for(Map.Entry<Integer, Integer> entry : clientToAppServerMapping.entrySet()) System.out.println("\nClient: " + entry.getKey() + " App server: " + entry.getValue());

        if(appServerPorts.get(currentPort) == 0) killAppServerIfEmpty(currentPort);
        return String.valueOf(appPortServiceNameMapping.get(portToMove));
    }

    /**
     * Function which kills an app server
     * @param port The port number on which the app server should be killed
     * */
    public void killAppServerIfEmpty(int port){
        System.out.println("No clients left on app server port: " + port);
        System.out.println("killing app server on port: " + port);
        MainAppServer mainAppServer = activeAppServers.get(port);
        try {
            mainAppServer.shutDown();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        activeAppServers.remove(port);
        //System.out.println("Active app Servers:");
    }



}
