package com.nixsolutions.filter;

import com.nixsolutions.entity.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/login";
        String adminURI = req.getContextPath() + "/admin";
        String userURI = req.getContextPath() + "/user";

        boolean loggedIn = session != null && session.getAttribute("loggedUser") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);

        if (!loggedIn && !loginRequest) {
            resp.sendRedirect(loginURI);
        }
        else if (loggedIn && loginRequest) {
            User user = (User) session.getAttribute("loggedUser");
            Long role = user.getRole().getRoleId();
            boolean isAdmin = role.equals(2L);
            boolean isUser = role.equals(1L);
            if (isAdmin) {
                resp.sendRedirect(adminURI);
            }
            else if(isUser) {
                resp.sendRedirect(userURI);
            }
            else
            {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
