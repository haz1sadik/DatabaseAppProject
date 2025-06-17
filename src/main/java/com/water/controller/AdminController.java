package com.water.controller;

import com.water.model.*;
import com.water.repository.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class AdminController {

    @Autowired private UserRepository userRepo;
    @Autowired private ActivityRepository activityRepo;
    @Autowired private WaterIntakeRepository waterRepo;
    @Autowired
    private WeightRepository weightRepo;

    @GetMapping("")
    public String adminDashboard(Model model) {
        List<User> nonAdminUsers = userRepo.findAll()
                .stream()
                .filter(user -> !"ROLE_ADMIN".equals(user.getRole()))
                .toList();
        model.addAttribute("users", nonAdminUsers);
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) return "redirect:/admin";

        // Get the latest weight if available
        Weight latestWeight = null;
        if (user.getWeights() != null && !user.getWeights().isEmpty()) {
            latestWeight = user.getWeights().stream()
                    .max((w1, w2) -> w1.getDateTime().compareTo(w2.getDateTime()))
                    .orElse(null);
        }

        model.addAttribute("user", user);
        model.addAttribute("latestWeight", latestWeight); // add to model
        return "admin_user";
    }



    @GetMapping("/user/{id}/activities")
    public String userActivities(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) return "redirect:/admin";
        model.addAttribute("activities", user.getActivities());
        model.addAttribute("userId", id);
        return "admin_activity_list";
    }

    @GetMapping("/user/{userId}/activity/{activityId}/edit")
    public String editActivity(@PathVariable Long userId, @PathVariable Long activityId, Model model) {
        Activity activity = activityRepo.findById(activityId).orElse(null);
        if (activity == null) return "redirect:/admin/user/" + userId + "/activities";

        // Set current weight value to the transient field for display
        if (activity.getWeight() != null) {
            activity.setWeightValueInput(activity.getWeight().getValue());
        }

        model.addAttribute("activity", activity);
        model.addAttribute("userId", userId);
        return "edit_activity";
    }

    @PostMapping("/user/{userId}/activity/{activityId}/edit")
    public String updateActivity(@PathVariable Long userId,
                                 @PathVariable Long activityId,
                                 @RequestParam("weightValue") double weightValue,
                                 @ModelAttribute Activity activityForm) {

        Activity existing = activityRepo.findById(activityId).orElse(null);
        if (existing == null) return "redirect:/admin/user/" + userId + "/activities";

        // Create and persist a new Weight
        Weight newWeight = new Weight();
        newWeight.setUser(existing.getUser());
        newWeight.setValue(weightValue);
        newWeight.setDateTime(activityForm.getDateTime()); // or LocalDateTime.now()
        newWeight = weightRepo.save(newWeight);

        // Update Activity fields
        existing.setWeight(newWeight);
        existing.setRunningDistance(activityForm.getRunningDistance());
        existing.setAvgPace(activityForm.getAvgPace());
        existing.setElevation(activityForm.getElevation());
        existing.setDateTime(activityForm.getDateTime());

        // Calculate kcal burnt
        double timeHours = existing.getRunningDistance() / existing.getAvgPace();
        double kcal = 8.3 * newWeight.getValue() * timeHours;
        existing.setKcalBurnt(kcal);

        activityRepo.save(existing);
        return "redirect:/admin/user/" + userId + "/activities";
    }



    @GetMapping("/user/{userId}/activity/{activityId}/delete")
    public String deleteActivity(@PathVariable Long userId, @PathVariable Long activityId) {
        activityRepo.deleteById(activityId);
        return "redirect:/admin/user/" + userId + "/activities";
    }

    @GetMapping("/user/{id}/water-intakes")
    public String userWaterIntakes(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) return "redirect:/admin";
        model.addAttribute("intakes", user.getWaterIntakes());
        model.addAttribute("userId", id);
        return "admin_water_list";
    }

    @GetMapping("/user/{userId}/water/{intakeId}/edit")
    public String editWaterIntake(@PathVariable Long userId, @PathVariable Long intakeId, Model model) {
        WaterIntake intake = waterRepo.findById(intakeId).orElse(null);
        if (intake == null) return "redirect:/admin/user/" + userId + "/water-intakes";
        model.addAttribute("intake", intake);
        model.addAttribute("userId", userId);
        return "edit_water";
    }

    @PostMapping("/user/{userId}/water/{intakeId}/edit")
    public String updateWaterIntake(@PathVariable Long userId,
                                    @PathVariable Long intakeId,
                                    @ModelAttribute WaterIntake intakeForm) {
        WaterIntake existing = waterRepo.findById(intakeId).orElse(null);
        if (existing == null) return "redirect:/admin/user/" + userId + "/water-intakes";

        existing.setAmount(intakeForm.getAmount());
        existing.setDateTime(intakeForm.getDateTime()); // optional

        waterRepo.save(existing);
        return "redirect:/admin/user/" + userId + "/water-intakes";
    }

    @GetMapping("/user/{userId}/water/{intakeId}/delete")
    public String deleteWaterIntake(@PathVariable Long userId, @PathVariable Long intakeId) {
        waterRepo.deleteById(intakeId);
        return "redirect:/admin/user/" + userId + "/water-intakes";
    }
}
