package com.tracker.gamification.controller;

import com.tracker.gamification.dto.LeaderboardEntryDto;
import com.tracker.gamification.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {
    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard(page, size));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<LeaderboardEntryDto>> getActivityLeaderboard(@PathVariable("activityId") Long activityId, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(leaderboardService.getActivityLeaderboard(activityId, page, size));
    }

    @GetMapping("/me")
    public ResponseEntity<Integer> getMyRank(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(leaderboardService.getMyRank(userId));
    }
}
