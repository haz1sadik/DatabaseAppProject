package com.water.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private int age;
    private double height;
    private String role; // ROLE_USER or ROLE_ADMIN

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Activity> activities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WaterIntake> waterIntakes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Weight> weights = new ArrayList<>();

    public User(Long id, List<WaterIntake> waterIntakes, List<Activity> activities, String role, double height, int age, String phone, String email, String password, String username) {
        this.id = id;
        this.waterIntakes = waterIntakes;
        this.activities = activities;
        this.role = role;
        this.height = height;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.username = username;
    }



    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<WaterIntake> getWaterIntakes() {
        return waterIntakes;
    }

    public void setWaterIntakes(List<WaterIntake> waterIntakes) {
        this.waterIntakes = waterIntakes;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Weight> getWeights() {
        return weights;
    }

    public void setWeights(List<Weight> weights) {
        this.weights = weights;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

}