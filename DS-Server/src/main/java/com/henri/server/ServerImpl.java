package com.henri.server;

import com.henri.RMI.Client.CallbackAppServerInterface;
import com.henri.dao.GameRepository;
import com.henri.dao.SessionIdentifierRepository;
import com.henri.dao.UserEntityRepository;
import com.henri.model.GameEntity;
import com.henri.model.SessionidentifierEntity;
import com.henri.model.UserEntity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

@Component("ServerImpl")
public class ServerImpl extends UnicastRemoteObject implements InterfaceServer {


    private Set<String> userSet = new HashSet<>();
    private Map<String, CallbackAppServerInterface> clientList;


    //DataSource dataSource;


    UserEntityRepository userEntityRepository;

    SessionIdentifierRepository sessionIdentifierRepository;

    GameRepository gameRepository;


    public ServerImpl(UserEntityRepository userEntityRepository, SessionIdentifierRepository sessionIdentifierRepository, GameRepository gameRepository) throws RemoteException {
        clientList = new HashMap<>();
        this.userEntityRepository = userEntityRepository;
        this.sessionIdentifierRepository = sessionIdentifierRepository;
        this.gameRepository = gameRepository;
        System.out.println("Repositories are set");

    }

    @Override
    public synchronized void registerForCallback(
            CallbackAppServerInterface callbackClientObject, String username)
            throws RemoteException {
        // store the callback object into the Map (only possible after the setup has been executed)
        if ((userSet.contains(username))) {
            clientList.put(username, callbackClientObject);
            System.out.println("Registered new client ");
            //doCallbacks();
        } // end if
    }

    @Override
    public String setupMessage(String username, String password) throws RemoteException {
        UserEntity user = userEntityRepository.findUserEntityByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return "true";
            } else {
                return "false";
            }
        } else {
            return "false";
        }


    }

    @Override
    public boolean checkUsername(String username) throws RemoteException {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        if (userEntity == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntityRepository.save(userEntity);
    }

    @Override
    public String acquireSessionId(String username) throws RemoteException {
        //find user
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        //initialize id generator
        IdGenerator idGenerator = new IdGenerator();
        //generate id
        String sessionId = idGenerator.generateId(60);
        //create new session object
        SessionidentifierEntity s = new SessionidentifierEntity();
        //set identifier to object
        s.setSessionIdentifier(sessionId);
        sessionIdentifierRepository.save(s);
        //set object to user
        userEntity.setSessionIdentifierEntity(s);
        //save user
        userEntityRepository.save(userEntity);
        //return the sessionId
        return sessionId;
    }

    @Override
    public void createGame(ArrayList<String> gamePositions, String username) throws RemoteException {
        // get number of players, game theme and positions
        String numberOfPlayers = gamePositions.get(0);
        String gameTheme = gamePositions.get(1);
        String gameName = gamePositions.get(2);
        ArrayList<String> positions = new ArrayList<>(gamePositions.subList(3, gamePositions.size()));

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < positions.size(); i++) {
            sb.append(positions.get(i));
            if (i != positions.size() - 1)
                sb.append(",");
        }

        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        GameEntity gameEntity = new GameEntity();
        gameEntity.setUserEntity(userEntity);
        gameEntity.setUserIdOne(userEntity.getUserId());
        gameEntity.setUserIdTurn(userEntity.getUserId());
        gameEntity.setGameSize(positions.size());
        gameEntity.setGamePositions(sb.toString());
        gameEntity.setGameTheme(Integer.parseInt(gameTheme));
        gameEntity.setGameMaxPlayers(Integer.parseInt(numberOfPlayers));
        gameEntity.setGameName(gameName);
        gameEntity.setGameUsers(1);
        gameRepository.save(gameEntity);
    }

    @Override
    public ArrayList<String> requestGames(String username) {

        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        ArrayList<GameEntity> games = gameRepository.findGameEntityByUserId(userEntity.getUserId());

        return createReturnGameArray(games);


    }

    @Override
    public ArrayList<String> requestAllGames(String username) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        ArrayList<GameEntity> games = gameRepository.findAllGames(userEntity.getUserId());

        return createReturnGameArray(games);
    }

    @Override
    public void requestJoin(int gameId, String username) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        GameEntity gameEntity = gameRepository.findGameEntityByGameId(gameId);
        int gameUsers = gameEntity.getGameUsers() + 1;
        gameEntity.setGameUsers(gameUsers);
        GameEntity g = setPlayerNumber(gameEntity, userEntity.getUserId());
        gameRepository.save(g);


    }

    public ArrayList<String> returnGameConfig(GameEntity gameEntity) {
        ArrayList<String> gameConfig = new ArrayList<>();
        gameConfig.add(String.valueOf(gameEntity.getGameId()));         //game ID
        gameConfig.add(gameEntity.getGameName());                       //Game name
        gameConfig.add(String.valueOf(gameEntity.getGameUsers()));      //number of users in the game
        gameConfig.add(String.valueOf(gameEntity.getGameMaxPlayers())); //game max players allowed
        gameConfig.add(String.valueOf(gameEntity.getGameTheme()));      //game theme
        gameConfig.add(gameEntity.getGamePositions());

        return gameConfig;
    }

    @Override
    public boolean checkTurn(int gameId, String username) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        GameEntity gameEntity = gameRepository.findGameEntityByGameId(gameId);
        if (gameEntity.getUserIdTurn() == userEntity.getUserId()) {
            return true;
        }

        return false;

    }

    @Override
    public ArrayList<String> requestGameConfig(int gameId) {
        GameEntity gameEntity = gameRepository.findGameEntityByGameId(gameId);
        return returnGameConfig(gameEntity);

    }

    @Override
    public boolean updateGame(int gameId, String username, String gamePositions, int score) throws RemoteException {
        GameEntity gameEntity = gameRepository.findGameEntityByGameId(gameId);
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        gameEntity.setGamePositions(gamePositions);
        GameEntity g = setScoreAndNextPlayer(gameEntity, userEntity.getUserId(), score);
        gameRepository.save(g);
        return true;
    }

    @Override
    public String requestGameWinner(int gameId) throws RemoteException {
        GameEntity gameEntity = gameRepository.findGameEntityByGameId(gameId);
        int scoreUserOne = (gameEntity.getUserOneScore());
        int scoreUserTwo = (gameEntity.getUserTwoScore());
        int scoreUserThree = (gameEntity.getUserThreeScore());
        int scoreUserFour = (gameEntity.getUserFourScore());

        if (scoreUserOne > scoreUserTwo && scoreUserOne > scoreUserThree && scoreUserOne > scoreUserFour) {
            return userEntityRepository.findUserEntityByUserId(gameEntity.getUserIdOne()).getUsername();
        } else if (scoreUserTwo > scoreUserOne && scoreUserTwo > scoreUserThree && scoreUserTwo > scoreUserFour) {
            return userEntityRepository.findUserEntityByUserId(gameEntity.getUserIdTwo()).getUsername();
        } else if (scoreUserThree > scoreUserOne && scoreUserThree > scoreUserTwo && scoreUserThree > scoreUserFour) {
            return userEntityRepository.findUserEntityByUserId(gameEntity.getUserIdThree()).getUsername();
        } else if (scoreUserFour > scoreUserOne && scoreUserFour > scoreUserThree && scoreUserFour > scoreUserThree) {
            return userEntityRepository.findUserEntityByUserId(gameEntity.getUserIdFour()).getUsername();
        }
        return null;
    }

    public ArrayList<String> createReturnGameArray(ArrayList<GameEntity> games) {
        ArrayList<String> returnGames = new ArrayList<>();
        for (GameEntity g : games) {
            returnGames.add(String.valueOf(g.getGameId()));         //game ID
            returnGames.add(g.getGameName());                       //game name
            returnGames.add(String.valueOf(g.getGameUsers()));      //number of users in the game
            returnGames.add(String.valueOf(g.getGameMaxPlayers())); //game max players allowed
            returnGames.add(String.valueOf(g.getGameTheme()));      //game theme
            returnGames.add(String.valueOf(g.getGameSize()));
            //returnGames.add(g.getActiveUsers()); //is comma separated string
        }
        return returnGames;
    }

    public GameEntity setScoreAndNextPlayer(GameEntity gameEntity, int userId, int score) {

        if (gameEntity.getUserIdOne() == userId) {
            gameEntity.setUserOneScore(gameEntity.getUserOneScore() + score);
            gameEntity.setUserIdTurn(gameEntity.getUserIdTwo());
        } else if (gameEntity.getUserIdTwo() == userId) {
            gameEntity.setUserTwoScore(gameEntity.getUserTwoScore() + score);
            if (gameEntity.getGameUsers() > 2) {
                gameEntity.setUserIdTurn(gameEntity.getUserIdThree());
            } else if (gameEntity.getGameUsers() == 2) {
                gameEntity.setUserIdTurn(gameEntity.getUserIdOne());
            }
        } else if (gameEntity.getUserIdThree() == userId) {
            gameEntity.setUserThreeScore(gameEntity.getUserThreeScore() + score);
            if (gameEntity.getGameUsers() > 3) {
                gameEntity.setUserIdTurn(gameEntity.getUserIdFour());
            } else if (gameEntity.getGameUsers() == 3) {
                gameEntity.setUserIdTurn(gameEntity.getUserIdOne());
            }
        } else if (gameEntity.getUserIdFour() == userId) {
            gameEntity.setUserFourScore(gameEntity.getUserFourScore() + score);
            gameEntity.setUserIdTurn(gameEntity.getUserIdOne());
        }

        return gameEntity;
    }

    public GameEntity setPlayerNumber(GameEntity gameEntity, int userId) {
        if (gameEntity.getUserIdTwo() == 0) {

            gameEntity.setUserIdTwo(userId);


        } else if (gameEntity.getUserIdThree() == 0) {

            gameEntity.setUserIdThree(userId);

        } else if (gameEntity.getUserIdFour() == 0) {

            gameEntity.setUserIdFour(userId);

        }

        return gameEntity;
    }


}
