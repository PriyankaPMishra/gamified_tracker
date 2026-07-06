package com.tracker.gamification.repository;

import com.tracker.gamification.dao.LevelTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelTrackerRepository extends JpaRepository<LevelTracker, Long> {
    Optional<LevelTracker> findByUserIdAndActivityId(
            Long userId,
            Long activityId
    );

    List<LevelTracker> findAllByUserId(Long userId);

    List<LevelTracker> findAllByActivityId(Long activityId);

    @Query("""
            SELECT COALESCE(SUM(l.totalXp), 0)
            FROM LevelTracker l
            WHERE l.userId = :userId
            """)
    Double getTotalXpByUserId(@Param("userId") Long userId);
}
