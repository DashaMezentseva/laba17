package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.entity.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        if (loggedUser.getRoleId() == 2) {
            JdbcUserDao jdbcUserDao = new JdbcUserDao();
            request.setAttribute("users", jdbcUserDao.findAll());
            request.setAttribute("adminName", loggedUser.getLogin());
            request.getServletContext().getRequestDispatcher("/admin/admin.jsp").forward(request, response);
        }
        if (loggedUser.getRoleId() == 1) {
            request.setAttribute("userName", loggedUser.getLogin());
            request.getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}


