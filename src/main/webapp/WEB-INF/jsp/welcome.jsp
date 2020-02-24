<%--
  Created by IntelliJ IDEA.
  User: Darya
  Date: 24.02.2020
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h2>
    Welcome to
    Today is <ftm:formatDate value="${today}" pattern="yyyy-MM-dd"/>
</h2>
</body>
</html>
