/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.IUserRepository;
import ch.heig.amt.overflow.domain.user.User;
import ch.heig.amt.overflow.domain.user.UserId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@ApplicationScoped
public class GamificationFacade {

    @Inject
    IGamificationEngine gamificationEngine;

    @Inject @Named("JdbcUserRepository")
    IUserRepository userRepository;

    public List<UserDTO> getLeaderboard() throws APINotReachableException {
        LeaderboardDTO leaderboardDTO = gamificationEngine.getLeaderboard();
        List<UserDTO> leaderboard = new ArrayList<>();
        leaderboardDTO.getLeaderboard().forEach((userDTO) -> {
            String username = userRepository.findById(new UserId(userDTO.getId())).map(User::getUsername).orElse("Unknown");
            userDTO.setUsername(username);
            leaderboard.add(userDTO);
        });
        return leaderboard;
    }

    public UserDTO getUserStats(UserId userId) throws APINotReachableException {
        return gamificationEngine.getUserStats(userId);
    }

}
