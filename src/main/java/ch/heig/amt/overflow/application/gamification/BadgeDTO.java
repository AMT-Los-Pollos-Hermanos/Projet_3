package ch.heig.amt.overflow.application.gamification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadgeDTO {

    private long id;

    private String name;

    private String description;

    private String icon;


}
