package ch.heig.amt.overflow.application.gamification;

import ch.heig.amt.overflow.domain.user.UserId;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class GamificationFacade {

    // TODO conf file
    private final String API_KEY = "edb9194d-3eb3-46a5-9e12-cea9e7391f7f";
    private final String API_ENDPOINT = "localhost:8080";
    private final String AUTH_HEADER_NAME = "X-API-KEY";

    public void sendEvent(EventDTO eventDTO) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/events"))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(eventDTO)))
                .build();

        try {
            HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // TODO handle response ? and exception ?

    }

    public LeaderboardDTO getLeaderboard() throws APINotReachableException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/leaderboard"))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), LeaderboardDTO.class);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new APINotReachableException("Impossible d'atteindre la ressource distante.");
        }
    }

    public UserDTO getUserStats(UserId userId) throws APINotReachableException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/users/" + userId))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), UserDTO.class);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new APINotReachableException("Impossible d'atteindre la ressource distante.");
        }
    }

}
