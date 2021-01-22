package ch.heig.amt.overflow.ui.web.api;

import ch.heig.amt.overflow.domain.api.ApiConfig;
import ch.heig.amt.overflow.infrastructure.gamification.GamificationEngine;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ApiEndpoint", urlPatterns = "/api")
public class ApiEndpoint extends HttpServlet {

    @Inject
    GamificationEngine gamificationEngine;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "application/json");
        resp.getWriter().write(new Gson().toJson(ApiConfig.builder()
                .apiEndpoint(gamificationEngine.getApiUrl())
                .apiKey(gamificationEngine.getApiKey())
                .build()));
    }

}
