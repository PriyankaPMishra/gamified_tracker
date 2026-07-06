package com.tracker.activity.dto;

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
    Integer totalXp;
    Integer currentLevelXp;
}
