package com.water.service;

import com.water.model.Activity;
import com.water.model.User;
import com.water.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {
    @Autowired private ActivityRepository activityRepo;

    public void save(Activity activity, User user) {
        activity.setUser(user);
        activity.setDateTime(LocalDateTime.now());
        double timeHours = activity.getRunningDistance() / activity.getAvgPace();
        double kcal = 8.3 * activity.getWeight().getValue() * timeHours;
        activity.setKcalBurnt(kcal);
        activityRepo.save(activity);
    }


    public List<Activity> findByUser(User user) {
        return user.getActivities();
    }
}