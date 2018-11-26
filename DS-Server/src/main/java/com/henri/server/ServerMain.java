package com.henri.server;

import com.henri.dao.GameRepository;
import com.henri.dao.SessionIdentifierRepository;
import com.henri.dao.UserEntityRepository;

import javax.sql.DataSource;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public void startServer(UserEntityRepository userEntityRepository, SessionIdentifierRepository sessionIdentifierRepository, GameRepository gameRepository) {
        try {
            //create on port 1114
            Registry registry = LocateRegistry.createRegistry(1114);

            //create a new service

            registry.rebind("ServerImplService", new ServerImpl(userEntityRepository, sessionIdentifierRepository, gameRepository));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }

}
