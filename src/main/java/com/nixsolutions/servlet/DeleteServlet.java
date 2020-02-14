package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.entity.User;
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
        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        String login = request.getParameter("userLogin");

        jdbcUserDao.remove(jdbcUserDao.findByLogin(login));
        request.getSession().setAttribute("users", jdbcUserDao.findAll());
        response.sendRedirect("/home");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}