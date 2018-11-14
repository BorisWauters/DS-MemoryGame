package com.henri.client.RMI;

import com.henri.RMI.Server.AppServerInterface;

import java.io.IOException;

public class ClientSendThread extends Thread{

    private AppServerInterface impl;

    public ClientSendThread(AppServerInterface impl, String username) throws IOException {
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
