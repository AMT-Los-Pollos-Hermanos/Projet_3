package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID id;

    private Double points;

    private List<BadgeDTO> badges;

    private List<UserReceivePointDTO> userReceivePointDTOs;

}
