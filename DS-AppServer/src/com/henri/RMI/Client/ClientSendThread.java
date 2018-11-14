package com.henri.RMI.Client;

import com.henri.server.InterfaceServer;

import java.io.IOException;

public class ClientSendThread extends Thread {

    private InterfaceServer impl;

    public ClientSendThread(InterfaceServer impl, String username) throws IOException {
        //this.socket = socket;
        this.impl = impl;
        //this.username = username;



    }

    public void run() {
        while (true) {
            System.out.println("Enter your message:");
            // read the message to deliver.
            //String msg = scn.nextLine();

            /*try {
                // write on the output stream
                impl.clientToServerMessage(username+","+msg);
                //dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
}
