package com.tracker.activity.dto;

import com.tracker.activity.dao.Activity;

import java.time.LocalDateTime;

public record ActivityLogResponseRecord(Long id,
                                        Long userId,
                                        Activity activity,
                                        LocalDateTime startTime,
                                        LocalDateTime endTime,
                                        Long durationMinutes,
                                        Double xpEarned,
                                        String notes,
                                        LocalDateTime createdAt) {
}
