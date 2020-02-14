package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User loggedUser = (User) req.getSession().getAttribute("loggedUser");
        if (loggedUser.getRoleId() == 2) {
            JdbcUserDao jdbcUserDao = new JdbcUserDao();
            req.setAttribute("users", jdbcUserDao.findAll());
            req.setAttribute("adminName", loggedUser.getLogin());
            req.getServletContext().getRequestDispatcher("/admin.jsp").forward(req, resp);
        }
        if (loggedUser.getRoleId() == 1) {
            req.setAttribute("userName", loggedUser.getLogin());
            req.getServletContext().getRequestDispatcher("/user.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
