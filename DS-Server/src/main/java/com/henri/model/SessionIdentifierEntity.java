package com.henri.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "session_identifier", schema = "ds")
public class SessionIdentifierEntity {
    private int sessionIdentifierId;
    private String sessionIdentifier;
    private Long cancellationTime = Long.valueOf(0);

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

    @Basic
    @Column(name = "cancellation_time")
    public long getCancellationTime(){return cancellationTime;}

    public void setCancellationTime(long cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionIdentifierEntity that = (SessionIdentifierEntity) o;
        return sessionIdentifierId == that.sessionIdentifierId &&
                Objects.equals(sessionIdentifier, that.sessionIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionIdentifierId, sessionIdentifier);
    }
}
