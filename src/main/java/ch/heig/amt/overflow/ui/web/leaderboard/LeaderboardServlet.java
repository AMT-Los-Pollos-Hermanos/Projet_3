package ch.heig.amt.overflow.ui.web.leaderboard;

import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.application.gamification.LeaderboardDTO;
import ch.heig.amt.overflow.domain.message.FlashMessage;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LeaderboardServlet", urlPatterns = "/leaderboard")
public class LeaderboardServlet {

    @Inject
    private GamificationFacade gamificationFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            LeaderboardDTO leaderboardDTO = gamificationFacade.getLeaderboard();
            request.setAttribute("leaderboard", leaderboardDTO);

        }catch (Exception e){
            request.getSession().setAttribute("flash", FlashMessage.builder()
                    .message(e.getMessage())
                    .type("danger")
                    .build());
            response.sendRedirect(request.getContextPath() + "/");
        }

        request.getSession().removeAttribute("flash");
    }
}
