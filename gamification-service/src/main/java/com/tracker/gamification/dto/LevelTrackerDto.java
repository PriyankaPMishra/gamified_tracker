package com.tracker.gamification.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class LevelTrackerDto {
    Long userId;
    Long activityId;
    Integer level;
    double totalXp;
    double currentLevelXp;
}
