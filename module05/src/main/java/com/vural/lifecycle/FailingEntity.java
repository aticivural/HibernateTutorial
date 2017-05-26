package com.vural.lifecycle;

/**
 * Created by vural on 25-May-17.
 */

import javax.persistence.*;

@Entity
public class FailingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    FailureStatus failureStatus = null;
    String value;

    public FailingEntity() {
    }

    public FailingEntity(FailureStatus failureStatus, String value) {
        this.failureStatus = failureStatus;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FailureStatus getFailureStatus() {
        return failureStatus;
    }

    public void setFailureStatus(FailureStatus failureStatus) {
        this.failureStatus = failureStatus;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailingEntity that = (FailingEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (failureStatus != that.failureStatus) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (failureStatus != null ? failureStatus.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    static enum FailureStatus {
        NOFAILURE, PREPERSIST, POSTPERSIST, POSTLOAD
    }

    @Override
    public String toString() {
        return "FailingEntity{" +
                "id=" + id +
                ", failureStatus=" + failureStatus +
                ", value='" + value + '\'' +
                '}';
    }

    @PrePersist
    void prePersist() {
        if (failureStatus.equals(FailureStatus.PREPERSIST)) {
            throw new RuntimeException("prepersist failure");
        }
    }

    @PostPersist
    void postPersist() {
        if (failureStatus.equals(FailureStatus.POSTPERSIST)) {
            throw new RuntimeException("postpersist failure");
        }
    }

    @PostLoad
    void postLoad() {
        if (failureStatus.equals(FailureStatus.POSTLOAD)) {
            throw new RuntimeException("postload failure");
        }
    }
}
