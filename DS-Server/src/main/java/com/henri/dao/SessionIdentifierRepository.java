package com.henri.dao;

import com.henri.model.SessionIdentifierEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("SessionIdentifierRepository")
public interface SessionIdentifierRepository extends CrudRepository<SessionIdentifierEntity, Integer> {

    //Custom queries
    /*@Query("select u from UserEntity u")
    List<UserEntity> findAllUsers();*/

    @Query("select s from SessionIdentifierEntity  s where s.sessionIdentifierId = :sessionId")
    SessionIdentifierEntity findSessionIdentifierById(@Param("sessionId") int sessionId);


}
