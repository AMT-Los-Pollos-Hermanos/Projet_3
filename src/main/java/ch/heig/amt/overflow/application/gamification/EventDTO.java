package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.UserId;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class EventDTO implements Serializable {

    private UserId userId;

    private Date timestamp;

    private String type;

    private Map<String, String> properties;
}
