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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("passwordsNotEqual");
        request.getSession().removeAttribute("dateNotCorrect");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        String role = request.getParameter("role");

        Pattern pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            request.getSession().setAttribute("passwordNotCorrect", true);
            response.sendRedirect("/add");
            return;
        }

        if (!password.equals(passwordAgain)) {
            LOG.trace("passwords are not equal");
            request.getSession().setAttribute("passwordsNotEqual", true);
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

        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        if (jdbcUserDao.findByLogin(login) != null) {
            LOG.trace("Another user has the same login. Change your login.");
            request.getSession().setAttribute("sameLogin", true);
            request.getServletContext().getRequestDispatcher("/add.jsp").forward(request, response);
            return;
        } else {
            request.getSession().removeAttribute("sameLogin");
        }

        if (jdbcUserDao.findByEmail(email) != null) {
            LOG.trace("Another user has the same email. Change your email.");
            request.getSession().setAttribute("sameEmail", true);
            request.getServletContext().getRequestDispatcher("/add.jsp").forward(request, response);
            return;
        } else {
            request.getSession().removeAttribute("sameEmail");
        }

        User newUser = new User(login, password, email, firstName, lastName, Date.valueOf(birthday), Long.valueOf(role));
        jdbcUserDao.create(newUser);
        //doGet(request, response);
        response.sendRedirect("/home");
    }

}



