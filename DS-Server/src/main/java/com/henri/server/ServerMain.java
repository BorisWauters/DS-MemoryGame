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
            //create on ort 1099
            Registry registry = LocateRegistry.createRegistry(1102);

            //create a new service named CounterService

            registry.rebind("ServerImplService", new ServerImpl(userEntityRepository, sessionIdentifierRepository, gameRepository));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("System is ready");
    }

}
