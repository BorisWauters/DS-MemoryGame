package com.henri.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "session_identifier", schema = "ds")
public class SessionidentifierEntity {
    private int sessionIdentifierId;
    private String sessionIdentifier;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_identifier_id")
    public int getSessionIdentifierId() {
        return sessionIdentifierId;
    }

    public void setSessionIdentifierId(int sessionIdentifierId) {
        this.sessionIdentifierId = sessionIdentifierId;
    }

    @Basic
    @Column(name = "session_identifier")
    public String getSessionIdentifier() {
        return sessionIdentifier;
    }

    public void setSessionIdentifier(String sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionidentifierEntity that = (SessionidentifierEntity) o;
        return sessionIdentifierId == that.sessionIdentifierId &&
                Objects.equals(sessionIdentifier, that.sessionIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionIdentifierId, sessionIdentifier);
    }
}
