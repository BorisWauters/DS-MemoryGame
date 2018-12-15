package com.henri.Dispatcher;

import com.henri.MainAppServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;

/**
 * Class which listens to runtime input
 * */
public class InputThread extends Thread {

    private Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            String s = sc.nextLine();
            System.out.println("command read: " + s);
            if (s.substring(0, 4).equals("kill")) {

                int port = Integer.parseInt(s.substring(5));
                System.out.println("killing app server on port: " + port);
                MainAppServer mainAppServer = DispatcherImpl.activeAppServers.get(port);
                try {
                    mainAppServer.shutDown();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
                DispatcherImpl.activeAppServers.remove(port);
                System.out.println("Active app Servers:");
                for (Map.Entry<Integer, MainAppServer> entry : DispatcherImpl.activeAppServers.entrySet()) {
                    System.out.println("port: " + entry.getKey());
                }
            }
            if(s.equals("show")){
                for (Map.Entry<Integer, MainAppServer> entry : DispatcherImpl.activeAppServers.entrySet()) {
                    System.out.println("port: " + entry.getKey());
                }
            }
        }
    }
}
