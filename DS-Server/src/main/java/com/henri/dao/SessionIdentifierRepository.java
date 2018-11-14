package com.henri.dao;

import com.henri.model.SessionidentifierEntity;
import com.henri.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("SessionIdentifierRepository")
public interface SessionIdentifierRepository extends CrudRepository<SessionidentifierEntity, Integer> {

    //Custom queries
    /*@Query("select u from UserEntity u")
    List<UserEntity> findAllUsers();*/


}
