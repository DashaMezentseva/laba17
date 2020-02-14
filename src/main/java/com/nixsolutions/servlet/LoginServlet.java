package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.dao.AbstractJdbcDao;
import com.nixsolutions.entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.dbcp2.BasicDataSource;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        User user = jdbcUserDao.findByLogin(login);

        if (user == null) {
            doGet(req, resp);
        } else if (user.getPassword().equals(password) && user.getLogin().equals(login)) {
            HttpSession session = req.getSession();
            session.setAttribute("loggedUser", user);
            resp.sendRedirect("/home");
        } else {
            doGet(req, resp);
        }
    }

}
