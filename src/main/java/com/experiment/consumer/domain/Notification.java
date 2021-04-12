package com.experiment.consumer.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "time")
    private Instant time;

    @Column(name = "origin")
    private String origin;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "user")
    private String user;

    @Column(name = "level")
    private String level;

    @Column(name = "acknowledged")
    private Boolean acknowledged;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getTime() {
        return this.time;
    }

    public Notification time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Notification origin(String origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTitle() {
        return this.title;
    }

    public Notification title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return this.user;
    }

    public Notification user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLevel() {
        return this.level;
    }

    public Notification level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getAcknowledged() {
        return this.acknowledged;
    }

    public Notification acknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
        return this;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", user='" + getUser() + "'" +
            ", level='" + getLevel() + "'" +
            ", acknowledged='" + getAcknowledged() + "'" +
            "}";
    }
}
