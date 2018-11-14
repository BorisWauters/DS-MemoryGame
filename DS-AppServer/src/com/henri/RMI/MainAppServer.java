package com.henri.RMI;

import com.henri.RMI.Client.ClientMain;
import com.henri.RMI.Server.ServerMain;

public class MainAppServer {

    public static void main (String[] args){
        ClientMain clientMain = new ClientMain();
        clientMain.startClient();

        ServerMain serverMain = new ServerMain();
        serverMain.startServer();
    }

}
