package com.nixsolutions.controller;

import com.nixsolutions.service.HibernateUserDao;
import com.nixsolutions.domain.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loggedUser");
        HibernateUserDao hibernateUserDao = new HibernateUserDao();
        String login = request.getParameter("userLogin");

        hibernateUserDao.remove(hibernateUserDao.findByLogin(login));
        request.getSession().setAttribute("users", hibernateUserDao.findAll());
        response.sendRedirect("/admin");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
