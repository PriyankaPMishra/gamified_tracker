package com.tracker.activity.service;

import com.tracker.activity.dao.Activity;
import com.tracker.activity.dto.ActivityResponseRecord;
import com.tracker.activity.dto.AddActivityRequestRecord;
import com.tracker.activity.exception.ActivityNotFoundException;
import com.tracker.activity.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    public ResponseEntity<ActivityResponseRecord> getActivity(String name) {
        Activity activity = activityRepository.findByName(name)
                .orElseThrow(() -> new ActivityNotFoundException("Activity not found: " + name));

        return ResponseEntity.ok(mapToResponse(activity));
    }

    public ResponseEntity<ActivityResponseRecord> addAcitvityEntity(AddActivityRequestRecord request) {
        Activity activity = mapToActivity(request);

        Activity savedActivity = activityRepository.save(activity);

        return ResponseEntity.ok(mapToResponse(savedActivity));
    }

    //    private Activity mapToActivity(AddActivityRequest activityRequest) {
//        return Activity.builder().name(activityRequest.getName()).category(activityRequest.getCategory()).description(activityRequest.getDescription()).xpMultiplier(activityRequest.getXpMultiplier()).active(activityRequest.isActive()).createdAt(LocalDateTime.now()).build();
//    }
//
//    private ActivityResponse mapToResponse(Activity activity) {
//        return ActivityResponse.builder().name(activity.getName()).category(activity.getCategory()).xpMultiplier(activity.getXpMultiplier()).description(activity.getDescription()).createdAt(activity.getCreatedAt()).build();
//    }
    private Activity mapToActivity(AddActivityRequestRecord activityRequest) {
        return Activity.builder()
                .name(activityRequest.name())
                .category(activityRequest.category())
                .description(activityRequest.description())
                .xpMultiplier(activityRequest.xpMultiplier())
                .active(activityRequest.active())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ActivityResponseRecord mapToResponse(Activity activity) {
        return new ActivityResponseRecord(
                activity.getName(),
                activity.getCategory(),
                activity.getXpMultiplier(),
                activity.isActive(),
                activity.getDescription(),
                activity.getCreatedAt()
        );
    }

    public ResponseEntity<List<ActivityResponseRecord>> getAllActivities() {
        List<ActivityResponseRecord> activities = activityRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(activities);
    }
}
