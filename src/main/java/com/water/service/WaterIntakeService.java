package com.water.service;

import com.water.model.WaterIntake;
import com.water.model.User;
import com.water.repository.WaterIntakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaterIntakeService {
    @Autowired private WaterIntakeRepository waterRepo;

    public void save(WaterIntake water, User user) {
        water.setUser(user);
        water.setDateTime(LocalDateTime.now());
        waterRepo.save(water);
    }

    public List<WaterIntake> findByUser(User user) {
        return user.getWaterIntakes();
    }
}