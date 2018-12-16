package com.henri.Server;

import com.henri.client.RMI.CallbackClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface class which lets the client access app server methods
 * */
public interface AppServerInterface extends Remote {

    /**
     * Function which enable clients to register their callback interfaces at the app server. this function is synchronized to ensure mutual exclusion.
     *
     * @param controllerId         The id of the controller which registers the callback and is used as the key in the @HashMap
     * @param callbackClientObject The object of the client callback interface
     * @param gameId               The Id of the game for which the callback is being registered
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void registerForCallback(int controllerId,
                             CallbackClientInterface callbackClientObject, int gameId
    ) throws RemoteException;

    /**
     * Function which removes callbacks from the HashMap
     *
     * @param controllerId The Id from the controller which initially requested the registration of the callback and now wants to remove it
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void removeCallback(int controllerId) throws RemoteException;

    /**
     * Function which checks the credentials of the user.
     *
     * @param username The name of the user
     * @param password The password of the user
     * @return String The response from the Database Server
     * @throws RemoteException Necessary because of the RMI
     *                         * @see RemoteException
     */
    String setupMessage(String username, String password) throws RemoteException;

    /**
     * Function which checks if the username has been taken yet.
     *
     * @param username The username of the user
     * @return boolean True if username hasn't been used yet, false if it has been
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    boolean checkUsername(String username) throws RemoteException;

    /**
     * Function which registers the user.
     *
     * @param username The name of the user
     * @param password The password of the user
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void registerUser(String username, String password) throws RemoteException;

    /**
     * Function which acquires the sessionId after login.
     *
     * @param username The name of the user
     * @return String The session Id generated by the Database Server
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    String acquireSessionId(String username) throws RemoteException;

    /**
     * Function which creates a new game depending on the game size.
     *
     * @param gameConfig String which contains the number of players, the game size and the game theme
     * @param username   The user who requested to create the game
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void createGame(String gameConfig, String username) throws RemoteException;

    /**
     * Function which requests the games in which the given user is active
     *
     * @param username The username of the user
     * @return ArrayList List with all the games and their information as strings
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    ArrayList<String> requestGames(String username) throws RemoteException;

    /**
     * Function which requests all the games in which the given user is not present
     *
     * @param username The username of the user
     * @return ArrayList List with all the games and their information as strings
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    ArrayList<String> requestAllGames(String username) throws RemoteException;

    /**
     * Function which requests the join of a game by a given user
     *
     * @param gameId   The Ii of the game the user would like to join
     * @param userId  The user id
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void requestJoin(int gameId, int userId) throws RemoteException;

    /**
     * Function which checks if the given user is at turn.
     *
     * @param gameId   Id of the game which has to be checked
     * @param userId The username of the user which wants to check if it's his turn
     * @return Boolean which is is true when it's the users turn and false if not
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    boolean checkTurn(int gameId, int userId) throws RemoteException;

    /**
     * Function which requests the current game configuration.
     *
     * @param gameId The Id of the game of which the configuration is requested
     * @return ArrayList List with the entire configuration of the game as strings
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    ArrayList<String> requestGameConfig(int gameId) throws RemoteException;

    /**
     * Function which updates a game. It will propagate this update to the Database Server and inform all clients which are currently viewing this game via their registered callbacks.
     *
     * @param gameId         The Id of the game which is to be updated. This is used to find all callbacks which need to be invoked.
     * @param userId       The username of the user which requests the update
     * @param gamePositions  Current settings of the memory cards
     * @param score          The score the user acquired during his turn
     * @param controllerType Used with the callback, required at the client side to cast to the right controller
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void updateGame(int gameId, int userId, ArrayList<String> gamePositions, int score, int controllerType) throws RemoteException;

    /**
     * Function which requests the game winner for a given game.
     *
     * @param gameId The Id o the given game
     * @return ArrayList List withe the name(s) of the winner(s) as strings
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    ArrayList<String> requestGameWinner(int gameId) throws RemoteException;

    /**
     * Function which updates current game viewers when a user flips a card. All registered callbacks for this game will be informed.
     *
     * @param buttonId       Id of the button which has been pressed, this enables other clients to figure out which button they need to de-activate en show the image of
     * @param gameId         The Id of the game which is being player, needed for the lookups in the HashMap which contains the callbacks for current viewers
     * @param controllerId   To make sure the player who is playing doe snot get the callback message
     * @param controllerType Required for correct casting at the client
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    void updateCardFlip(int buttonId, int gameId, int controllerId, int controllerType) throws RemoteException;

    /**
     * Function which checks if the sessionIdentifier is still valid
     *
     * @param sessionId         The data base ID of the session Id
     * @param sessionIdentifier The current session Id
     * @return boolean Returns true when the Id is still active, false if not
     * @throws RemoteException Necessary because of the RMI
     * @see RemoteException
     */
    boolean checkSessionIdentifier(int sessionId, String sessionIdentifier) throws RemoteException;

    /**
     * Function which requests the overall top players in the application.
     *
     * @return ArrayList List with the names and scores of the top players as strings
     */
    ArrayList<String> requestTopPlayers() throws RemoteException;

    /**
     * Function which lets a player enter a certain game.
     *
     * @param gameId The id of the game
     * @return Game The game which the user entered for backup purposes
     * */
    Game enterGame(int gameId) throws RemoteException;

    /**
     * Function which checks whether the game is active on the current app server
     *
     * @param gameId The id of the game
     * @return boolean True if the game resides on this app server, false otherwise
     * */
    boolean checkGameAvailability(int gameId) throws RemoteException;

    /**
     * Function which checks if the game is residing on another app server
     *
     * @param gameId The id of the game
     * @return int The port of the app server if the game resides on another app server, -1 otherwise
     * */
    int checkGameOnOtherServer(int gameId) throws RemoteException;

    /**
     * Function which lets a user enter a new game and stores this game on the app server
     *
     * @param gameId The id of the game
     * @return Game The game object for backup purposes
     * */
    Game enterNewGame(int gameId) throws RemoteException;

    /**
     * Function which lets a user leave the game.
     *
     * @param gameId The id of the game
     * */
    void leaveGame(int gameId) throws RemoteException;

    void sendMessage(String message, String username, int gameId, int controllerType) throws  RemoteException;

}
