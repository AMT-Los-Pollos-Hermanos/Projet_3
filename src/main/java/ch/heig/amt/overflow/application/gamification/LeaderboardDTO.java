package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class LeaderboardDTO {
    Map<UUID, Double> leaderboard;
}
