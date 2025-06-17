package com.water.repository;

import com.water.model.User;
import com.water.model.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRepository extends JpaRepository<Weight, Long> {

    Weight findTopByUserOrderByDateTimeDesc(User user);
}

