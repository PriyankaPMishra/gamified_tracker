package com.tracker.gamification.dto;

public record LeaderboardEntryDto(int rank, Long userId, double totalXp) {
}
