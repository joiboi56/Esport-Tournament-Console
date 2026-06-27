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

    private static final List<String> ADMIN_ONLY = List.of("/teams");

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
        if (ADMIN_ONLY.stream().anyMatch(path::startsWith)
                && user.getRole() != Role.ADMIN) {
            res.sendRedirect("/home");
            return false;
        }
        return true;
    }
}