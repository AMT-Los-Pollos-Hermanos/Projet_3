package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.UserId;

public interface IGamificationEngine {
    void sendEvent(EventDTO eventDTO);

    LeaderboardDTO getLeaderboard() throws APINotReachableException;

    UserDTO getUserStats(UserId userId) throws APINotReachableException;
}
