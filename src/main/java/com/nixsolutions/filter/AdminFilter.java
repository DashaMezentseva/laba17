package com.nixsolutions.filter;

import com.nixsolutions.domain.User;
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

//@WebServlet("/home")

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession(false);
        String userURI = req.getContextPath() + "/user";

        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (!loggedIn) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            User user = (User) session.getAttribute("loggedUser");
            Long role = user.getRole().getRoleId();
            boolean isUser = role.equals(1L);
            if (isUser) {
                resp.sendRedirect(userURI);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}

