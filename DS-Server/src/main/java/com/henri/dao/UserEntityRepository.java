package com.henri.dao;

import com.henri.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("UserEntityRepository")
public interface UserEntityRepository extends CrudRepository<UserEntity, Integer> {

    //Custom queries
    /*@Query("select u from UserEntity u")
    List<UserEntity> findAllUsers();*/

    @Query("select u from UserEntity  u where u.username = :username")
    UserEntity findUserEntityByUsername(@Param("username") String username);

    @Query("select u from UserEntity  u where  u.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") int userId);

}
