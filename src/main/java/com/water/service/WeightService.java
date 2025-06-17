package com.water.service;

import com.water.model.User;
import com.water.model.Weight;
import com.water.repository.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeightService {
    @Autowired
    private WeightRepository weightRepo;

    public Weight getLatestWeightByUser(User user) {
        return weightRepo.findTopByUserOrderByDateTimeDesc(user);
    }

    public void saveWeight(double value, User user) {
        Weight weight = new Weight(value, LocalDateTime.now(), user);
        weightRepo.save(weight);
    }
}
