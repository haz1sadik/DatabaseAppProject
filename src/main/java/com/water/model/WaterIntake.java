package com.water.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class WaterIntake {
    @Id @GeneratedValue
    private Long id;

    private double amount;
    private LocalDateTime dateTime;

    @ManyToOne
    private User user;

    public WaterIntake(Long id, User user, double amount, LocalDateTime dateTime) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.dateTime = dateTime;
    }
    public WaterIntake() {}

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WaterIntake{" +
                "id=" + id +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", user=" + user +
                '}';
    }
}
