package com.henri.RMI;

import com.henri.RMI.Client.ClientMain;
import com.henri.RMI.Server.ServerMain;

/**
 * Class which is used to run the App Server
 * */
public class MainAppServer {

    /**
     * Main function which initializes the client and server capabilities of the App Server.
     * */
    public static void main (String[] args){
        ClientMain clientMain = new ClientMain();
        clientMain.startClient(Integer.parseInt(args[0]));

        ServerMain serverMain = new ServerMain();
        serverMain.startServer(Integer.parseInt(args[1]));
    }

}
