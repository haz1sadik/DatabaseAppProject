package com.water.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Weight {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "weight_value")
    private double value;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Weight() {}

    public Weight(double value, LocalDateTime dateTime, User user) {
        this.value = value;
        this.dateTime = dateTime;
        this.user = user;
    }

    public Weight(Long id, LocalDateTime dateTime, User user, double value) {
        this.id = id;
        this.dateTime = dateTime;
        this.user = user;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "id=" + id +
                ", value=" + value +
                ", dateTime=" + dateTime +
                ", user=" + user +
                '}';
    }
}
