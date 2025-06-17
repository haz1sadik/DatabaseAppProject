package com.water.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Weight weight;

    private double elevation;
    private double avgPace;
    private double runningDistance;
    private LocalDateTime dateTime;
    private double kcalBurnt;

    @ManyToOne
    private User user;

    @Transient
    private double weightValueInput; // For admin editing form only

    public Activity() {}

    public Activity(Long id, User user, double kcalBurnt, LocalDateTime dateTime,
                    double runningDistance, double avgPace, double elevation, Weight weight) {
        this.id = id;
        this.user = user;
        this.kcalBurnt = kcalBurnt;
        this.dateTime = dateTime;
        this.runningDistance = runningDistance;
        this.avgPace = avgPace;
        this.elevation = elevation;
        this.weight = weight;
    }

    // Getters and Setters

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

    public double getKcalBurnt() {
        return kcalBurnt;
    }

    public void setKcalBurnt(double kcalBurnt) {
        this.kcalBurnt = kcalBurnt;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getRunningDistance() {
        return runningDistance;
    }

    public void setRunningDistance(double runningDistance) {
        this.runningDistance = runningDistance;
    }

    public double getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(double avgPace) {
        this.avgPace = avgPace;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public double getWeightValueInput() {
        return weightValueInput;
    }

    public void setWeightValueInput(double weightValueInput) {
        this.weightValueInput = weightValueInput;
    }

    @Override
    public String toString() {
        return "Activity{id=" + id + ", date=" + dateTime + ", userId=" + user.getId() + "}";
    }
}
