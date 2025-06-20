package com.water.controller;

import com.water.model.*;
import com.water.repository.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class MainController {

    @Autowired private UserRepository userRepo;
    @Autowired private ActivityRepository activityRepo;
    @Autowired private WaterIntakeRepository waterRepo;
    @Autowired private BCryptPasswordEncoder encoder;
    @Autowired private WeightRepository weightRepo;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute User user, @RequestParam("weightValue") double weightValue) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepo.save(user);

        Weight weight = new Weight(weightValue, LocalDateTime.now(), user);
        weightRepo.save(weight);

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        if (user.getRole().equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "home";
    }

    @GetMapping("/activity")
    public String activityPage(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());

        // Get latest weight
        Weight latestWeight = weightRepo.findTopByUserOrderByDateTimeDesc(user);

        Activity activity = new Activity();
        activity.setWeight(latestWeight); // pre-fill weight for new activity

        model.addAttribute("activity", activity);
        return "activity";
    }



    @PostMapping("/activity")
    public String saveActivity(@ModelAttribute Activity activity, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        activity.setUser(user);
        activity.setDateTime(LocalDateTime.now());

        // Re-fetch the full Weight object from DB using ID
        Long weightId = activity.getWeight().getId();
        Weight fullWeight = weightRepo.findById(weightId).orElse(null);
        activity.setWeight(fullWeight);

        double timeHours = activity.getRunningDistance() / activity.getAvgPace();
        double kcal = 0;

        if (fullWeight != null) {
            kcal = 8.3 * fullWeight.getValue() * timeHours;
        }

        activity.setKcalBurnt(kcal);
        activityRepo.save(activity);
        return "redirect:/activity/list";
    }



    @GetMapping("/activity/list")
    public String activityList(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("activities", user.getActivities());
        return "activity_list";
    }

    @GetMapping("/water-intake")
    public String waterPage(Model model) {
        model.addAttribute("waterIntake", new WaterIntake());
        return "water";
    }

    @PostMapping("/water-intake")
    public String saveWater(@ModelAttribute WaterIntake water, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        water.setUser(user);
        water.setDateTime(LocalDateTime.now());
        waterRepo.save(water);
        return "redirect:/water-intake/list";
    }

    @GetMapping("/water-intake/list")
    public String waterList(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("intakes", user.getWaterIntakes());
        return "water_list";
    }

    @GetMapping("/userprofile")
    public String showUserProfile(Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        // Get latest weight and add separately
        Weight latestWeight = weightRepo.findTopByUserOrderByDateTimeDesc(user);
        double latestWeightValue = latestWeight != null ? latestWeight.getValue() : 0.0;
        model.addAttribute("latestWeightValue", latestWeightValue);

        return "userprofile";
    }

    @GetMapping("/userMain")
    public String showProfile(Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        // Get latest weight and add separately
        Weight latestWeight = weightRepo.findTopByUserOrderByDateTimeDesc(user);
        double latestWeightValue = latestWeight != null ? latestWeight.getValue() : 0.0;
        model.addAttribute("latestWeightValue", latestWeightValue);

        return "userMain";
    }




    // --- NEW: Update user profile ---
    @PostMapping("/userprofile")
    public String updateUserProfile(
            @ModelAttribute("user") User formUser,
            @RequestParam(name = "newWeight", required = false) Double newWeight,
            Principal principal) {

        User user = userRepo.findByUsername(principal.getName());
        if (user != null) {
            user.setPhone(formUser.getPhone());
            user.setAge(formUser.getAge());
            user.setHeight(formUser.getHeight());
            userRepo.save(user);

            if (newWeight != null && newWeight > 0) {
                Weight weight = new Weight(newWeight, LocalDateTime.now(), user);
                weightRepo.save(weight);
            }
        }
        return "redirect:/userprofile";
    }


    // --- Optional: Redirect root URL to login ---
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}
