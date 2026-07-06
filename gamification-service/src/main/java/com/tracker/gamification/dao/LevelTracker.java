package com.tracker.gamification.dao;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "LevelTracker")
public class LevelTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    Long activityId;
    Integer level;
    double totalXp;
    double currentLevelXp;
}
