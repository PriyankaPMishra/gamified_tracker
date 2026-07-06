package com.tracker.gamification.service;

import com.tracker.gamification.dao.ActivityLevelThreshold;
import com.tracker.gamification.dto.ActivityLevelThresholdDto;
import com.tracker.gamification.misc.ActivityLevelThresholdId;
import com.tracker.gamification.repository.ActivityLevelThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ActivityLevelThresholdService {

    @Autowired
    public ActivityLevelThresholdRepository activityLevelThresholdRepository;

    public ActivityLevelThresholdDto getActivityLevelThresholdById(ActivityLevelThresholdDto activityLevelThresholdDto) {
        Optional<ActivityLevelThreshold> activityLevelThreshold = activityLevelThresholdRepository.findById(mapToEntity(activityLevelThresholdDto).getId());
        if (activityLevelThreshold.isPresent()) {
            return mapToDto(activityLevelThreshold.get());
        } else throw new NoSuchElementException("ActivityLevelThreshold not found");
    }

    public ActivityLevelThresholdDto saveActivityLevelThreshold(ActivityLevelThresholdDto activityLevelThresholdDto) {
        ActivityLevelThreshold activityLevelThreshold = mapToEntity(activityLevelThresholdDto);
        activityLevelThresholdRepository.save(activityLevelThreshold);
        return mapToDto(activityLevelThreshold);
    }

    public ActivityLevelThreshold mapToEntity(ActivityLevelThresholdDto dto) {

        return ActivityLevelThreshold.builder()
                .id(
                        ActivityLevelThresholdId.builder()
                                .activityId(dto.getActivityId())
                                .level(dto.getLevel())
                                .build()
                )
                .xpRequired(dto.getXpRequired())
                .build();
    }

    public ActivityLevelThresholdDto mapToDto(ActivityLevelThreshold entity) {

        return ActivityLevelThresholdDto.builder()
                .activityId(entity.getId().getActivityId())
                .level(entity.getId().getLevel())
                .xpRequired(entity.getXpRequired())
                .build();
    }

    public List<ActivityLevelThresholdDto> getAllActivityLevelThreshold() {

        return activityLevelThresholdRepository.findAll().stream().map(this::mapToDto).toList();
    }
}
