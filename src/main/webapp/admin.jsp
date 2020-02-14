<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="usersListTag" prefix="myTags" %>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8" />
		<title>Admin's Page</title>
		 <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
            <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar-right">
		<div class="message">
		        Admin ${adminName} <a href="Logout">(Logout)</a>
                <br/>
		</div>
	</nav>

<br/>
<a href="/add">Add new User</a>
<br/>


<myTags:table userList="${users}"/>



</body>
</html>