package com.nixsolutions.servlet;

import com.nixsolutions.HibernateUserDao;
import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
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

@WebServlet("/add")
public class AddServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AddServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getServletContext().getRequestDispatcher("/admin/add.jsp").forward(request, response);
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("passwordsNotEqual");
        request.getSession().removeAttribute("dateNotCorrect");
        request.getSession().removeAttribute("loginNotCorrect");
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("emailNotCorrect");
        request.getSession().removeAttribute("firstNameNotCorrect");
        request.getSession().removeAttribute("lastNameNotCorrect");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String id = request.getParameter("id");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        //String role = request.getParameter("role");
        Long role = Long.valueOf(request.getParameter("role"));

        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$");
        Matcher matcher = pattern.matcher(login);
        if (!matcher.matches()) {
            request.getSession().setAttribute("loginNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        if (!password.equals(passwordAgain)) {
            LOG.trace("passwords are not equal");
            request.getSession().setAttribute("passwordsNotEqual", true);
            response.sendRedirect("/add");
            return;
        }

        pattern = Pattern.compile("^[0-9a-zA-Z]+$");
        matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            request.getSession().setAttribute("passwordNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
        matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            request.getSession().setAttribute("emailNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        pattern = Pattern.compile("^[A-Z]{1}[a-z]{1,25}");
        matcher = pattern.matcher(firstName);
        if (!matcher.matches()) {
            request.getSession().setAttribute("firstNameNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        pattern = Pattern.compile("^[A-Z]{1}[a-z]{1,25}");
        matcher = pattern.matcher(lastName);
        if (!matcher.matches()) {
            request.getSession().setAttribute("lastNameNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }



        LocalDate localDate = Date.valueOf(birthday).toLocalDate();
        LocalDate now = LocalDate.now();
        if (localDate.isAfter(now)) {
            LOG.trace("This is incorrect date");
            request.getSession().setAttribute("dateNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        HibernateUserDao hibernateUserDao = new HibernateUserDao();
        if (hibernateUserDao.findByLogin(login) != null) {
            LOG.trace("Another user has the same login. Change your login.");
            request.getSession().setAttribute("sameLogin", true);
            request.getServletContext().getRequestDispatcher("/admin/add.jsp").forward(request, response);
            return;
        } else {
            request.getSession().removeAttribute("sameLogin");
        }

        if (hibernateUserDao.findByEmail(email) != null) {
            LOG.trace("Another user has the same email. Change your email.");
            request.getSession().setAttribute("sameEmail", true);
            request.getServletContext().getRequestDispatcher("/admin/add.jsp").forward(request, response);
            return;
        } else {
            request.getSession().removeAttribute("sameEmail");
        }
        String nameOfRole = "";
        if (role == 1L)
            nameOfRole = "user";
        if (role == 2L)
            nameOfRole = "admin";

        User newUser = new User(login, password, email, firstName, lastName, Date.valueOf(birthday), new Role(role, nameOfRole));
        hibernateUserDao.create(newUser);

        response.sendRedirect("/admin");

    }

}



