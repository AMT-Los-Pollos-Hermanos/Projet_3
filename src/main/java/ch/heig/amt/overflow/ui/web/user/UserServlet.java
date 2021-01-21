package ch.heig.amt.overflow.ui.web.user;

import ch.heig.amt.overflow.application.BusinessException;
import ch.heig.amt.overflow.application.gamification.GamificationFacade;
import ch.heig.amt.overflow.application.gamification.UserDTO;
import ch.heig.amt.overflow.domain.message.FlashMessage;
import ch.heig.amt.overflow.domain.user.UserId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {

    @Inject
    private GamificationFacade gamificationFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserId userId = new UserId(request.getPathInfo().split("/")[1]);

        try{
            // Get user
            UserDTO userDTO = gamificationFacade.getUserStats(userId);
            request.setAttribute("user", userDTO);
            request.getRequestDispatcher("/WEB-INF/views/user.jsp").forward(request, response);
            request.getSession().removeAttribute("flash");
        }catch(Exception e){
            request.getSession().setAttribute("flash", FlashMessage.builder()
                    .message(e.getMessage())
                    .type("danger")
                    .build());
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

}
