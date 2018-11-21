package com.henri;

import javax.sql.DataSource;

import com.henri.dao.GameRepository;
import com.henri.dao.SessionIdentifierRepository;
import com.henri.dao.UserEntityRepository;
import com.henri.server.ServerMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.henri.dao")
@Configuration
@ComponentScan(basePackages = {"com.henri.dao", "com.henri.model","com.henri.server"})
public class MainDB implements CommandLineRunner {


    @Autowired
    DataSource dataSource;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    SessionIdentifierRepository sessionIdentifierRepository;

    @Autowired
    GameRepository gameRepository;


    public static void main(String[] args) {
        SpringApplication.run(MainDB.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting server");
        ServerMain main = new ServerMain();
        main.startServer(userEntityRepository, sessionIdentifierRepository, gameRepository);


    }


}

