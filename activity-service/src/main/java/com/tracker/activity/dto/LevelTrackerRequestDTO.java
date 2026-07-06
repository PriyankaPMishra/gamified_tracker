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
public class LevelTrackerRequestDTO {
    Long userId;
    Long activityId;
    double xp;
}
