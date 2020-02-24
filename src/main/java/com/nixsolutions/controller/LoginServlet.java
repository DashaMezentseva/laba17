package com.nixsolutions.controller;

import com.nixsolutions.service.HibernateUserDao;
import com.nixsolutions.domain.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        HibernateUserDao hibernateUserDao = new HibernateUserDao();
        User user = hibernateUserDao.findByLogin(login);

        if (user == null) {
            doGet(request, response);
        } else if (user.getPassword().equals(password) && user.getLogin().equals(login)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            if (user.getRole().getRoleId() == 2) {
                request.setAttribute("users", hibernateUserDao.findAll());
                request.setAttribute("adminName", user.getLogin());
                request.getServletContext().getRequestDispatcher("/admin/admin.jsp").forward(request, response);
            }
            if (user.getRole().getRoleId() == 1) {
                request.setAttribute("userName", user.getLogin());
                request.getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
            }
        } else {
            doGet(request, response);
        }
    }

}
