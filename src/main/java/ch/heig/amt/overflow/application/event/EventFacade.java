package ch.heig.amt.overflow.application.event;

import ch.heig.amt.overflow.domain.event.Event;
import ch.heig.amt.overflow.domain.user.UserId;
import com.google.gson.Gson;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class EventFacade {

    private final String API_KEY = "88980fa7-7167-46d5-bbe7-367a204b7bd2";
    private final String API_ENDPOINT = "localhost:8080";
    private final String AUTH_HEADER_NAME = "X-API-KEY";

    public void sendEvent(Event event) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/events"))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(event)))
                .build();

        try {
            HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // TODO handle response ? and exception ?

    }

    public void getLeaderboard() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/leaderboards"))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // TODO handle response
    }

    public void getUserStats(UserId userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "/users/" + userId))
                .headers(AUTH_HEADER_NAME, API_KEY)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // TODO Handle response
    }

}
