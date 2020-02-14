package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
import com.nixsolutions.entity.User;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AddServlet.class);
    User editUser;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String login = request.getParameter("userLogin");
        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        editUser = jdbcUserDao.findByLogin(login);
        request.getSession().setAttribute("editUser", editUser);
        request.getServletContext().getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        User userEdit = (User) request.getAttribute("editUser");

        String passwordEdit = "";

        if (userEdit.getPassword() != null) {
            passwordEdit = userEdit.getPassword();
        }


        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        String role = request.getParameter("role");

        String regex;

        regex = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        if (!password.isEmpty()) {

            if (!password.matches(regex)) {
                LOG.trace("Password hasn`t a true format");
                response.sendRedirect("/edit");
            }

            if (!password.equals(passwordAgain)) {
                LOG.trace("passwords are not equal");
                response.sendRedirect("/edit");
            }
        }

        regex = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}";
        if (!email.matches(regex)) {
            LOG.trace("Email hasn`t a true format");
            response.sendRedirect("/edit");
        }

        regex = "^[A-Z]{1}[a-z]{1,25}";
        if (!firstName.matches(regex)) {
            LOG.trace("First name hasn`t a true format");
            response.sendRedirect("/edit");
        }

        regex = "^[A-Z]{1}[a-z]{1,25}";
        if (!lastName.matches(regex)) {
            LOG.trace("Last name hasn`t a true format");
            response.sendRedirect("/edit");
        }


        regex = "\\d{4}-\\d{2}-\\d{2}";

        if (!birthday.matches(regex)) {
            LOG.trace("Date isn`t true! True format is 2013-02-21.");
            response.sendRedirect("/edit");
        }

        LocalDate localDate = Date.valueOf(birthday).toLocalDate();
        LocalDate now = LocalDate.now();
        if (localDate.isAfter(now)) {
            LOG.trace("This is incorrect date");
            response.sendRedirect("/edit");
            return;
        }

        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        if (password == "" || password.isEmpty()) {
            password = passwordEdit;
        } else if (jdbcUserDao.findByEmail(email) != null) {
            LOG.trace("Another user has the same email. Change your email.");
            request.getServletContext().getRequestDispatcher("/edit.jsp").forward(request, response);

        } else {
            User newUser = new User(login, password, email, firstName, lastName, Date.valueOf(birthday), Long.parseLong(role));
            jdbcUserDao.update(newUser);
            doGet(request, response);
        }

    }


}
