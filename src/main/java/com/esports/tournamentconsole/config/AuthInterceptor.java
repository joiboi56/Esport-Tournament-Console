package com.esports.tournamentconsole.config;

import com.esports.tournamentconsole.model.Role;
import com.esports.tournamentconsole.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final List<String> ADMIN_ONLY = List.of("/teams", "/teams/{id}/delete");

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {
        String path = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("loggedUser");

        if (user == null) {
            res.sendRedirect("/login");
            return false;
        }

        // Only block the teams LIST and DELETE — not register
        boolean isAdminOnly = (path.equals("/teams") || path.matches("/teams/\\d+/delete"));

        if (isAdminOnly && user.getRole() != Role.ADMIN) {
            res.sendRedirect("/home");
            return false;
        }

        return true;
    }
}