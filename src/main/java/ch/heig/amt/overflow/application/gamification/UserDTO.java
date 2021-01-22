/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID id;

    private Integer points;

    private String username;

    private String lastName;

    private String firstName;

    private List<BadgeDTO> badges;

    private List<UserReceivePointDTO> userReceivePointDTOs;

}
