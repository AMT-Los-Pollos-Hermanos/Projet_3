package ch.heig.amt.overflow.domain.event;

import ch.heig.amt.overflow.domain.user.UserId;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class Event implements Serializable {
    private UserId userId;
    private String timestamp;
    private String type;
    private Map<String, String> properties;
}
