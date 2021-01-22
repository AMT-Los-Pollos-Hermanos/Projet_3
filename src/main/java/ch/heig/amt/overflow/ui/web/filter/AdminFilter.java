package ch.heig.amt.overflow.ui.web.filter;

import ch.heig.amt.overflow.application.auth.UserDTO;
import ch.heig.amt.overflow.domain.message.FlashMessage;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = "/admin")
public class AdminFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        UserDTO loggedUser = (UserDTO) session.getAttribute("currentUser");
        if(loggedUser != null && loggedUser.getIsAdmin()) {
            chain.doFilter(request, response);
        } else {
            req.getSession().setAttribute("flash", FlashMessage.builder()
                    .message("Vous n'avez pas accès à cette page")
                    .type("danger")
                    .build());
            ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/");
        }
    }

}
