package com.tracker.gateway.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class ActivityLevelThresholdDto {
    private Long activityId;
    private Integer level;
    private double xpRequired;
}
