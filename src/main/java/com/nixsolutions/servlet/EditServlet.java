package com.nixsolutions.servlet;

import com.nixsolutions.JdbcUserDao;
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

@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AddServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        String login = request.getParameter("userLogin");

        User user = jdbcUserDao.findByLogin(login);
        request.getSession().setAttribute("editUser", user);
        String password = user.getPassword();

        request.getSession().setAttribute("oldPassword", password);
        request.getServletContext().getRequestDispatcher("/edit.jsp").forward(request, response);
        request.getSession().removeAttribute("passwordNotCorrect");
        request.getSession().removeAttribute("passwordsNotEqual");
        request.getSession().removeAttribute("dateNotCorrect");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        JdbcUserDao jdbcUserDao = new JdbcUserDao();

        String oldPassword = (String) request.getSession().getAttribute("oldPassword");

        String id = request.getParameter("id");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthday = request.getParameter("birthday");
        String role = request.getParameter("role");

        if (password == "null" || password.isEmpty() || password == "NULL") {
            password = oldPassword;
        }

//        Pattern pattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
//        Matcher matcher = pattern.matcher(password);
//        if (!matcher.matches()) {
//            request.getSession().setAttribute("passwordNotCorrect", true);
//            response.sendRedirect("/add");
//            return;
//        }

        else if (!password.equals(passwordAgain)) {
            LOG.trace("passwords are not equal");
            request.getSession().setAttribute("passwordsNotEqual", true);
            response.sendRedirect("/edit?userLogin=" + login);
            return;
        }

        LocalDate localDate = Date.valueOf(birthday).toLocalDate();
        LocalDate now = LocalDate.now();
        if (localDate.isAfter(now)) {
            LOG.trace("This is incorrect date");
            request.getSession().setAttribute("dateNotCorrect", true);
            response.sendRedirect("/edit?userLogin=" + login);
            return;
        }

//        JdbcUserDao jdbcUserDao = new JdbcUserDao();

        User newUser = new User(Long.valueOf(id), login, password, email, firstName, lastName, Date.valueOf(birthday), Long.valueOf(role));
        jdbcUserDao.update(newUser);
        response.sendRedirect("/home");


    }


}
