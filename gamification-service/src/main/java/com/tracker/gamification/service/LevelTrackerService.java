package com.tracker.gamification.service;

import com.tracker.gamification.dao.ActivityLevelThreshold;
import com.tracker.gamification.dao.LevelTracker;
import com.tracker.gamification.dto.LevelTrackerDto;
import com.tracker.gamification.dto.LevelTrackerRequestDTO;
import com.tracker.gamification.repository.ActivityLevelThresholdRepository;
import com.tracker.gamification.repository.LevelTrackerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Transactional
public class LevelTrackerService {

    private final LevelTrackerRepository levelTrackerRepository;

    private final ActivityLevelThresholdRepository activityLevelThresholdRepository;

    public List<LevelTrackerDto> findByUserId(Long userId) {
        return levelTrackerRepository.findAllByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<LevelTrackerDto> findByActivityId(Long activityId) {
        return levelTrackerRepository.findAllByActivityId(activityId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<LevelTrackerDto> findAll() {
        return levelTrackerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public LevelTrackerDto findById(Long id) {
        LevelTracker levelTracker = levelTrackerRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "LevelTracker with id: " + id + " not found"
                        )
                );

        return mapToDto(levelTracker);
    }

    @Transactional
    public LevelTrackerDto save(LevelTrackerRequestDTO dto) {

        LevelTracker levelTracker = levelTrackerRepository
                .findByUserIdAndActivityId(
                        dto.getUserId(),
                        dto.getActivityId()
                )
                .map(existingTracker -> {
                    existingTracker.setTotalXp(
                            existingTracker.getTotalXp() + dto.getXp()
                    );
                    return existingTracker;
                })
                .orElseGet(() -> LevelTracker.builder()
                        .userId(dto.getUserId())
                        .activityId(dto.getActivityId())
                        .totalXp(dto.getXp())
                        .build());

        List<ActivityLevelThreshold> reachedLevels =
                activityLevelThresholdRepository
                        .findReachedLevels(
                                levelTracker.getActivityId(),
                                levelTracker.getTotalXp(),
                                PageRequest.of(0, 1)
                        );

        if (!reachedLevels.isEmpty()) {
            updateLevelProgress(levelTracker, reachedLevels.get(0));
        } else {
            levelTracker.setLevel(1);
            levelTracker.setCurrentLevelXp(levelTracker.getTotalXp());
        }

        LevelTracker savedLevelTracker =
                levelTrackerRepository.save(levelTracker);

        return mapToDto(savedLevelTracker);
    }

    private void updateLevelProgress(
            LevelTracker levelTracker,
            ActivityLevelThreshold threshold
    ) {

        levelTracker.setLevel(
                threshold.getId().getLevel()
        );

        levelTracker.setCurrentLevelXp(
                levelTracker.getTotalXp() - threshold.getXpRequired()
        );
    }

    private LevelTrackerDto mapToDto(LevelTracker entity) {

        return LevelTrackerDto.builder()
                .userId(entity.getUserId())
                .activityId(entity.getActivityId())
                .level(entity.getLevel())
                .currentLevelXp(entity.getCurrentLevelXp())
                .build();
    }

    private LevelTracker mapToEntity(LevelTrackerDto dto) {

        return LevelTracker.builder()
                .userId(dto.getUserId())
                .activityId(dto.getActivityId())
                .level(dto.getLevel())
                .currentLevelXp(dto.getCurrentLevelXp())
                .build();
    }
}
