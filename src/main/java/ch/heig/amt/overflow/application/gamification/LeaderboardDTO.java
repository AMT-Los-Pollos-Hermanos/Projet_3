/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class LeaderboardDTO {
    List<UserDTO> leaderboard;
}
