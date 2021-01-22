package ch.heig.amt.overflow.domain.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiConfig {

    private String apiKey;
    private String apiEndpoint;

}
