package com.henri.dao;


import com.henri.model.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository("GameRepository")
public interface GameRepository  extends CrudRepository<GameEntity,Integer> {

    @Query("select g from GameEntity g where g.userEntity.userId = :userId or g.userIdOne = :userId or g.userIdTwo = :userId or g.userIdThree = :userId or g.userIdFour = :userId")
    ArrayList<GameEntity> findGameEntityByUserId(@Param("userId") int userId);

    @Query("select g from GameEntity g where g.userEntity.userId <> :userId and g.userIdOne <> :userId and g.userIdTwo <> :userId and g.userIdThree <> :userId and g.userIdFour <> :userId")
    ArrayList<GameEntity> findAllGames(@Param("userId") int userId);

    @Query("select g from GameEntity  g where g.gameId = :gameId")
    GameEntity findGameEntityByGameId(@Param("gameId") int gameId);
}
