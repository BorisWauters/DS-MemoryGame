package com.henri.Dispatcher;

import com.henri.RMI.MainAppServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DispatcherImpl extends UnicastRemoteObject implements DispatcherInterface  {

    private Map<Integer, Integer> appServerPorts = new HashMap();
    private ArrayList<Integer> databaseServerPorts = new ArrayList<>();
    private Map<Integer, Integer> clientToAppServerMapping = new HashMap<>();

    public DispatcherImpl() throws RemoteException {
        appServerPorts.put(1104,0);
        appServerPorts.put(1105,0);
        appServerPorts.put(1106,0);
        appServerPorts.put(1107,0);
        appServerPorts.put(1108,0);
        appServerPorts.put(1109,0);
        appServerPorts.put(1110,0);
        appServerPorts.put(1111,0);
        appServerPorts.put(1112,0);
        appServerPorts.put(1113,0);

        databaseServerPorts.add(1114);
        databaseServerPorts.add(1113);
        databaseServerPorts.add(1115);
        System.out.println("lists initialized");

    }

    public int setup(int clientId) throws RemoteException{
        int i = 0;
        int databasePort = 0;
        for(Map.Entry<Integer, Integer> entry : appServerPorts.entrySet()){
            if(entry.getValue() == 0){
                clientToAppServerMapping.put(clientId, entry.getKey());
                MainAppServer mainAppServer = new MainAppServer();
                if(i % 3 == 0){
                    databasePort = databaseServerPorts.get(0);
                }else if(i % 3 == 1){
                    databasePort = databaseServerPorts.get(1);
                }else if(i % 3 == 2){
                    databasePort = databaseServerPorts.get(2);
                }
                String[] args = new String[2];
                args[0] = String.valueOf(databasePort);
                args[1] = String.valueOf(entry.getKey());
                mainAppServer.main(args);

                System.out.println("Connected to Appserver on port: " + entry.getKey() + " and database server on port:" + databasePort);

                return entry.getKey();
            }else if(entry.getValue() < 20){    //should be more than 20, (per 20 active games we would start a new app server)
                clientToAppServerMapping.put(clientId, entry.getKey());
                return entry.getKey();
            }
            i++;
        }
        return 0;
    }

    public void remove(int clientId) throws RemoteException{
        //decrease the number of active clients
        for(Map.Entry<Integer, Integer> entry : appServerPorts.entrySet()){
            if(entry.getKey() == clientToAppServerMapping.get(clientId)) entry.setValue(entry.getValue() - 1);
        }
        clientToAppServerMapping.remove(clientId);

    }


}
