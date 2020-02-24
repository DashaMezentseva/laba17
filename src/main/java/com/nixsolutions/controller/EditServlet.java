package com.nixsolutions.controller;

import com.nixsolutions.service.HibernateUserDao;
import com.nixsolutions.domain.Role;
import com.nixsolutions.domain.User;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HibernateUserDao hibernateUserDao = new HibernateUserDao();
        Long id = Long.valueOf(request.getParameter("userId"));

        User user = hibernateUserDao.findById(id);
        Long roleId = user.getRole().getRoleId();
        request.getSession().setAttribute("roleId", roleId);

        request.getSession().setAttribute("editUser", user);
        String password = "";
        if (user != null) {
            password = user.getPassword();
        }

        request.getSession().setAttribute("oldPassword", password);
        request.getServletContext().getRequestDispatcher("/admin/edit.jsp").forward(request, response);
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("passwordsNotEqual");
        request.getSession().removeAttribute("dateNotCorrect");
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("emailNotCorrect");
        request.getSession().removeAttribute("firstNameNotCorrect");
        request.getSession().removeAttribute("lastNameNotCorrect");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        String oldPassword = (String) request.getSession().getAttribute("oldPassword");

        String id = (request.getParameter("id"));
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        Long role = Long.valueOf(request.getParameter("role"));

        if (password.isEmpty() && passwordAgain.isEmpty()) {
            password = oldPassword;
            passwordAgain = oldPassword;
        }

        if (!password.equals(passwordAgain)) {
            LOG.trace("passwords are not equal");
            request.getSession().setAttribute("passwordsNotEqual", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            request.getSession().setAttribute("passwordNotCorrect", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
        matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            request.getSession().setAttribute("emailNotCorrect", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        pattern = Pattern.compile("^[A-Z]{1}[a-z]{1,25}");
        matcher = pattern.matcher(firstName);
        if (!matcher.matches()) {
            request.getSession().setAttribute("firstNameNotCorrect", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        pattern = Pattern.compile("^[A-Z]{1}[a-z]{1,25}");
        matcher = pattern.matcher(lastName);
        if (!matcher.matches()) {
            request.getSession().setAttribute("lastNameNotCorrect", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        LocalDate localDate = Date.valueOf(birthday).toLocalDate();
        LocalDate now = LocalDate.now();
        if (localDate.isAfter(now)) {
            LOG.trace("This is incorrect date");
            request.getSession().setAttribute("dateNotCorrect", true);
            response.sendRedirect("/edit?userId=" + id);
            return;
        }

        String nameOfRole = "";
        if (role == 1L) {
            nameOfRole = "user";
        }
        if (role == 2) {
            nameOfRole = "admin";
        }

        HibernateUserDao hibernateUserDao = new HibernateUserDao();
        User newUser = new User(Long.valueOf(id), login, password, email, firstName, lastName, Date.valueOf(birthday), new Role(role, nameOfRole));
        hibernateUserDao.update(newUser);
        response.sendRedirect("/admin");


    }


}
