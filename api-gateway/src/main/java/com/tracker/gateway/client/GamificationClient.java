package com.tracker.gateway.client;


import com.tracker.gateway.dto.ActivityLevelThresholdDto;
import com.tracker.gateway.dto.LevelTrackerDto;
import com.tracker.gateway.dto.LevelTrackerRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "gamification-service")
public interface GamificationClient {

    // =========================
    // Level Tracker APIs
    // =========================

    @GetMapping("/level")
    List<LevelTrackerDto> getAllLevelTracker();

    @GetMapping("/level/{id}")
    LevelTrackerDto getLevelTrackerById(
            @PathVariable("id") Long id
    );

    @PostMapping("/level")
    LevelTrackerDto createLevelTracker(
            @RequestBody LevelTrackerRequestDTO request
    );

    @GetMapping("/level/user/{userId}")
    List<LevelTrackerDto> getLevelTrackerByUserId(
            @PathVariable("userId") Long userId
    );

    @GetMapping("/level/activity/{activityId}")
    List<LevelTrackerDto> getLevelTrackerByActivityId(
            @PathVariable("activityId") Long activityId
    );

    // =========================
    // Activity Level Threshold APIs
    // =========================

    @GetMapping("/activity-level-threshold")
    List<ActivityLevelThresholdDto> getActivityLevelThreshold();

    @PostMapping("/activity-level-threshold/activity")
    ActivityLevelThresholdDto getActivityLevelThresholdById(
            @RequestBody ActivityLevelThresholdDto activityLevelThresholdDto
    );

    @PostMapping("/activity-level-threshold")
    ActivityLevelThresholdDto createActivityLevelThreshold(
            @RequestBody ActivityLevelThresholdDto activityLevelThresholdDto
    );
}
