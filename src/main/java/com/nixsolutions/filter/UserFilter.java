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

@WebFilter("/user")
public class UserFilter implements Filter {

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
        String adminURI = req.getContextPath() + "/admin";

        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (!loggedIn) {
            chain.doFilter(request, response);
        } else {
            User user = (User) session.getAttribute("loggedUser");
            Long role = user.getRole().getRoleId();
            boolean isAdmin = role.equals(2L);
            if (isAdmin) {
                resp.sendRedirect(adminURI);
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
