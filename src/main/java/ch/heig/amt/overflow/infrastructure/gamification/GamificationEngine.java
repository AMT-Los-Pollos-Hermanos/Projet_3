/*
 * AMT : Project 3 - Overflow
 * Authors : Gil Balsiger, Chris Barros Henriques, Julien Béguin & Gaëtan Daubresse
 * Date : 08.01.2021
 */

package ch.heig.amt.overflow.infrastructure.gamification;

import ch.heig.amt.overflow.application.gamification.*;
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
public class GamificationEngine implements IGamificationEngine {

    private final String API_KEY;
    private final String API_ENDPOINT;
    private final String API_URL;
    private final String AUTH_HEADER_NAME = "X-API-KEY";

    public GamificationEngine() {
        String apiHost = System.getenv("API_HOST") != null ? System.getenv("API_HOST") : "localhost";
        String apiPort = System.getenv("API_PORT") != null ? System.getenv("API_PORT") : "8080";
        API_KEY = System.getenv("API_KEY") != null ? System.getenv("API_KEY") : "88980fa7-7167-46d5-bbe7-367a204b7bd2";
        API_ENDPOINT = "http://" + apiHost + ":" + apiPort;
        API_URL = System.getenv("API_URL") != null ? System.getenv("API_URL") : "http://localhost:8080";
    }

    public String getApiKey() {
        return API_KEY;
    }

    public String getApiEndpoint() {
        return API_ENDPOINT;
    }

    public String getApiUrl() {
        return API_URL;
    }

    @Override
    public void sendEvent(EventDTO eventDTO) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/events"))
                .header(AUTH_HEADER_NAME, API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(eventDTO)))
                .build();

        HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public LeaderboardDTO getLeaderboard() throws APINotReachableException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/leaderboard?nbUsers=10&page=0"))
                .header(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), LeaderboardDTO.class);

        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new APINotReachableException("Impossible d'atteindre la ressource distante.");
        }
    }

    @Override
    public UserDTO getUserStats(UserId userId) throws APINotReachableException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/users/" + userId.toString()))
                .header(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), ch.heig.amt.overflow.application.gamification.UserDTO.class);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            return null;
        }
    }

}
