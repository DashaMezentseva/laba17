package com.nixsolutions.tag;

import static java.time.temporal.ChronoUnit.YEARS;

import com.nixsolutions.entity.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserListTag implements Tag {

    private static final Logger logger = LoggerFactory
        .getLogger(UserListTag.class);

    private PageContext pageContext;
    private Tag tag;

    private List<User> userList;

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override
    public void setParent(Tag tag) {
        this.tag = tag;
    }

    @Override
    public Tag getParent() {
        return tag;
    }

    @Override
    public int doStartTag() throws JspException {
        //int count = 1;
        StringBuilder stringBuilder = new StringBuilder();
        JspWriter out = pageContext.getOut();

        stringBuilder.append("<table class=\"table table-hover table-bordered\">\n");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>");
        stringBuilder.append("Login");
        stringBuilder.append("</td>");
        stringBuilder.append("<td>");
        stringBuilder.append("First name");
        stringBuilder.append("</td>");
        stringBuilder.append("<td>");
        stringBuilder.append("Last name");
        stringBuilder.append("</td>");
        stringBuilder.append("<td>");
        stringBuilder.append("Age");
        stringBuilder.append("</td>");
        stringBuilder.append("<td>");
        stringBuilder.append("Role");
        stringBuilder.append("</td>");
        stringBuilder.append("<td>");
        stringBuilder.append("Actions");
        stringBuilder.append("</td>");
        stringBuilder.append("</tr>");
        stringBuilder.append("<tbody>\n");

        if (userList != null) {
            for (User user : userList) {
                stringBuilder.append("<tr>");
                stringBuilder.append("<td>");
                stringBuilder.append(user.getLogin());
                stringBuilder.append("</td>");
                stringBuilder.append("<td>");
                stringBuilder.append(user.getFirstName());
                stringBuilder.append("</td>");
                stringBuilder.append("<td>");
                stringBuilder.append(user.getLastName());
                stringBuilder.append("</td>");
                stringBuilder.append("<td>");
                stringBuilder.append(ChronoUnit.YEARS.between(user.getBirthday().toLocalDate(), LocalDate.now()));
                stringBuilder.append("</td>");
                stringBuilder.append("<td>");
                stringBuilder.append(user.getRoleId() == 2 ? "Admin" : "User");
                stringBuilder.append("</td>");
                stringBuilder.append("<td>");
                stringBuilder.append("<form action=\"/edit\"  method=\"get\">\n")
                    .append("<input type=\"hidden\" name=\"userLogin\" value=")
                    .append(user.getLogin()).append(">\n")
                    .append("<button type=\"submit\" class=\"btn btn-info ")
                    .append("\">Edit</button>\n")
                    .append("<a href=\"/delete?userLogin=").append(user.getLogin()).append("\"")
                    .append("class=\"btn btn-danger\" role=\"button\" onclick=\"return confirm('Are you sure?')  ")
                    .append("\">Delete</a>")
                    .append("</form>");
                stringBuilder.append("</td>");
                stringBuilder.append("</tr>");
            }
            stringBuilder.append("</tbody>");
            stringBuilder.append("</table>");
        }

        try {
            out.print(stringBuilder.toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    @Override
    public void release() {

    }
}