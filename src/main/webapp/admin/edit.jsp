<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit User</title>
     <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar-right">
		<div class="message">
		Admin ${adminName}
		<a href="Logout">(Logout)</a>
		</div>
	</nav>

<h1>Edit User</h1>

<form method="post" class="form-horizontal" action="/edit">
    <input type="hidden" name="id" value="${editUser.id}">
    <input type="hidden" name="action" value="${editUser.login == null ? 'create' : 'update'}">
    <div class="form-group">
        <label class="control-label col-sm-3"
               for="login">Login:</label>
        <div class="col-sm-6">
            <input
                    readonly="readonly"
                    type="text"
                    class="form-control"
                    id="login"
                    name="login"
                    value="${editUser.login}">
        </div>

    </div>

    <div class="form-group">
                <label class="control-label col-sm-3"
                   for="password">Password:</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="password" pattern="^[0-9a-zA-Z]+$" minlength="4" maxlength="20"
                        title="  Should be minimum 4 characters.It can be letters and numbers"
                       placeholder="Enter password" name="password"
                       aria-describedby="passwordDescribe"  >
                        <small id="passwordDescribe" class="text-muted">
                        Should be minimum 4 characters.It can be letters and numbers.
                        </small>
                    </div>

                    <div class="col-sm-offset-3 col-sm-6 err-message">
                                               <c:if test="${passwordNotCorrect != null}">
                                                   <div class="alert alert-danger" role="alert">
                                                       This password isn't correct.
                                                        minimum 4 characters.It can be letters and numbers.
                                                   </div>
                                               </c:if>
                                           </div>
<div class="col-sm-offset-3 col-sm-6 err-message">
                    <c:if test="${passwordsNotEqual != null}">
                         <div class="alert alert-danger" role="alert">
                         Passwords are not equal. Correct them.
                         </div>
                    </c:if>
                    </div>
    </div>

        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="passwordAgain">Password again:</label>
            <div class="col-sm-6">
                <input type="password" class="form-control" id="passwordAgain"
                       placeholder="Password again" name="passwordAgain" aria-describedby="passwordAgainDescribe" >
                       <small id="passwordAgainDescribe" class="text-muted">
                       Enter the new password again.
                       </small>
            </div>
            <div class="col-sm-offset-3 col-sm-6 err-message">
                                <c:if test="${passwordsNotEqual != null}">
                                    <div class="alert alert-danger" role="alert">
                                        Passwords aren`t equal!
                                    </div>
                                </c:if>
                            </div>

        </div>

        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="email">Email:</label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                       title="Correct pattern characters@characters.domain"
                       placeholder="Enter email" name="email" aria-describedby="emailDescribe" value="${editUser.email}" required>
                       <small id="emailDescribe" class="text-muted">
                       Example *****@***.com
                       </small>

            </div>
            <div class="col-sm-offset-3 col-sm-6 err-message">
                        <c:if test="${emailNotCorrect != null}">
                            <div class="alert alert-danger" role="alert">
                                This email isn't correct. Example *****@***.com
                            </div>
                        </c:if>
                    </div>
                    <div class="col-sm-offset-3 col-sm-6 err-message">
                                                        <c:if test="${sameEmail != null}">
                                                            <div class="alert alert-danger" role="alert">
                                                                This email already exists.
                                                            </div>
                                                        </c:if>
                                            </div>
                    </div>

        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="First Name">First Name:</label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="First Name" pattern="^[A-Z]{1}[a-z]{1,25}"
                       title="The first letter must be uppercase"
                       placeholder="Enter first name" name="firstName" aria-describedby="firstNameDescribe" value="${editUser.firstName}" required>
                <small id="firstNameDescribe" class="text-muted">
                    The first letter must be uppercase.
                </small>
            </div>

<div class="col-sm-offset-3 col-sm-6 err-message">
                    <c:if test="${firstNameNotCorrect != null}">
                        <div class="alert alert-danger" role="alert">
                            This first name is not correct. The first letter must be uppercase.

                        </div>
                    </c:if>
                </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="Last Name">Last Name:</label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="Last Name" pattern="^[A-Z]{1}[a-z]{1,25}"
                title="The first letter must be uppercase"
                       placeholder="Enter last name" name="lastName" aria-describedby="lastNameDescribe" value="${editUser.lastName}" required>
                <small id="lastNameDescribe" class="text-muted">
                    The first letter must be uppercase.
                </small>
            </div>
            <div class="col-sm-offset-3 col-sm-6 err-message">
                                        <c:if test="${lastNameNotCorrect != null}">
                                            <div class="alert alert-danger" role="alert">
                                                This last name is not correct. The first letter must be uppercase.

                                            </div>
                                        </c:if>
                                    </div>
                            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="Birthday">Birthday:</label>
            <div class="col-sm-6">
                <input type="date" class="form-control" id="Birthday"
                       placeholder="Enter birthday" name="birthday" value="${editUser.birthday}" required>
            </div>
            <div class="col-sm-offset-3 col-sm-6 err-message">
                         <c:if test="${dateNotCorrect != null}">
                         <div class="alert alert-danger" role="alert">
                              This date is not correct.
                         </div>
                         </c:if>
                    </div>
        </div>

        <div class="form-group">
            <label class="control-label col-sm-3"
                   for="Role">Role:</label>
            <div class="col-sm-6">
                <select class="form-control" id="Role" name="role">
                    <option value="1">User</option>
                    <option value="2">Admin</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-1 col-sm-offset-4">
                <button type="submit" class="btn btn-success">  OK  </button>
            </div>
            <div class="col-sm-1">
                <a href="/admin"
                   class="btn btn-primary"
                   role="button"
                   aria-pressed="true">Cancel</a>
            </div>
        </div>
    </form>

    </body>
    </html>