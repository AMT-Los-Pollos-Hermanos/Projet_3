package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.UserId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GamificationFacade {

    @Inject
    IGamificationEngine gamificationEngine;

    public LeaderboardDTO getLeaderboard() throws APINotReachableException {
        return gamificationEngine.getLeaderboard();
    }

    public UserDTO getUserStats(UserId userId) throws APINotReachableException {
        return gamificationEngine.getUserStats(userId);
    }

}
