package com.henri.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "ds")
public class UserEntity{
    private int userId, score;
    private String username;
    private String password;
    private SessionIdentifierEntity sessionidentifierEntity;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "score")
    public int getScore(){ return  score;}

    public void setScore(int score){ this.score = score;}

    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_identifier_session_identifier_Id")
    public SessionIdentifierEntity getSessionIdentifierEntity() {
        return sessionidentifierEntity;
    }

    public void setSessionIdentifierEntity(SessionIdentifierEntity s) {
        sessionidentifierEntity = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId == that.userId &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password);
    }
}
