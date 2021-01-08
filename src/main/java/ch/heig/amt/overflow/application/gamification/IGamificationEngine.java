/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.UserId;

public interface IGamificationEngine {
    void sendEvent(EventDTO eventDTO);

    LeaderboardDTO getLeaderboard() throws APINotReachableException;

    UserDTO getUserStats(UserId userId) throws APINotReachableException;
}
