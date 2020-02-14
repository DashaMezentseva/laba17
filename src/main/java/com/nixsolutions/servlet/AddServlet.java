package com.nixsolutions.servlet;

import com.nixsolutions.JdbcRoleDao;
import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.dao.UserDao;
import com.nixsolutions.entity.User;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.el.ELClass;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

@WebServlet("/add")
public class AddServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AddServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getServletContext().getRequestDispatcher("/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

//        String id = request.getParameter("id");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        String role = request.getParameter("role");

//        String regex = "[0-9]";
//        if (!id.matches(regex)) {
//            LOG.trace("Id hasn`t true format");
//            response.sendRedirect("/add");
//        }

        String regex;
        regex = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
        if (!login.matches(regex)) {
            LOG.trace("Login hasn`t true format");
            response.sendRedirect("/add");
        }

        regex = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        if (!password.matches(regex)) {
            LOG.trace("Password hasn`t a true format");
            response.sendRedirect("/add");
        }

        if (!password.equals(passwordAgain)) {
            LOG.trace("passwords are not equal");
            response.sendRedirect("/add");
        }

        regex = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}";
        if (!email.matches(regex)) {
            LOG.trace("Email hasn`t a true format");
            response.sendRedirect("/add");
        }

        regex = "^[A-Z]{1}[a-z]{1,25}";
        if (!firstName.matches(regex)) {
            LOG.trace("First name hasn`t a true format");
            response.sendRedirect("/add");
        }

        regex = "^[A-Z]{1}[a-z]{1,25}";
        if (!lastName.matches(regex)) {
            LOG.trace("Last name hasn`t a true format");
            response.sendRedirect("/add");
        }


        regex = "\\d{4}-\\d{2}-\\d{2}";

        if (!birthday.matches(regex)) {
            LOG.trace("Date isn`t true! True format is 2013-02-21.");
            response.sendRedirect("/add");
        }

        LocalDate localDate = Date.valueOf(birthday).toLocalDate();
        LocalDate now = LocalDate.now();
        if (localDate.isAfter(now)) {
            LOG.trace("This is incorrect date");
            response.sendRedirect("/add");
            return;
        }

        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        if (jdbcUserDao.findByLogin(login) != null) {
            LOG.trace("Another user has the same login. Change your login.");
            request.getServletContext().getRequestDispatcher("/add.jsp").forward(request, response);
        } else if (jdbcUserDao.findByEmail(email) != null) {
            LOG.trace("Another user has the same email. Change your email.");
            request.getServletContext().getRequestDispatcher("/add.jsp").forward(request, response);

        } else {
            User newUser = new User(login, password, email, firstName, lastName, Date.valueOf(birthday), Long.parseLong(role));
            jdbcUserDao.create(newUser);
            doGet(request, response);
        }

    }


}
