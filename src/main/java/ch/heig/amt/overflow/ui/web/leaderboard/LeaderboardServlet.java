package ch.heig.amt.overflow.ui.web.leaderboard;

import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.application.gamification.LeaderboardDTO;
import ch.heig.amt.overflow.application.gamification.UserDTO;
import ch.heig.amt.overflow.domain.message.FlashMessage;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LeaderboardServlet", urlPatterns = "/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    @Inject
    private GamificationFacade gamificationFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<UserDTO> leaderboardDTO = gamificationFacade.getLeaderboard();
            request.setAttribute("leaderboard", leaderboardDTO);
            request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
            request.getSession().removeAttribute("flash");
        }catch (Exception e){
            request.getSession().setAttribute("flash", FlashMessage.builder()
                    .message(e.getMessage())
                    .type("danger")
                    .build());
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
