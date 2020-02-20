package com.nixsolutions.servlet;

import com.nixsolutions.DataSource;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        JdbcUserDao jdbcUserDao = new JdbcUserDao(new DataSource().getDataSource());
        User user = jdbcUserDao.findByLogin(login);

        if (user == null) {
            doGet(request, response);
        } else if (user.getPassword().equals(password) && user.getLogin().equals(login)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", user);
            if (user.getRoleId() == 2) {
                request.setAttribute("users", jdbcUserDao.findAll());
                request.setAttribute("adminName", user.getLogin());
                //response.sendRedirect("/admin/admin.jsp");
                request.getServletContext().getRequestDispatcher("/admin/admin.jsp").forward(request, response);
            }
            if (user.getRoleId() == 1) {
                request.setAttribute("userName", user.getLogin());
                //response.sendRedirect("/user.jsp");
                request.getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
            }
        } else {
            doGet(request, response);
        }
    }

}
