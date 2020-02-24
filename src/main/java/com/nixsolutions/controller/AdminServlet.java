package com.nixsolutions.controller;

import com.nixsolutions.service.HibernateUserDao;
import com.nixsolutions.domain.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User loggedUser = (User) request.getSession().getAttribute("loggedUser");
        if (loggedUser.getRole().getRoleId() == 2L) {
            HibernateUserDao hibernateUserDao = new HibernateUserDao();
            List<User> users = hibernateUserDao.findAll();
            request.setAttribute("users", hibernateUserDao.findAll());
            request.setAttribute("adminName", loggedUser.getLogin());
            request.getServletContext().getRequestDispatcher("/admin/admin.jsp").forward(request, response);
        }
        if (loggedUser.getRole().getRoleId() == 1L) {
            request.setAttribute("userName", loggedUser.getLogin());
            request.getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}


