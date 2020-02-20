
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

    <title>Login</title>
<style type="text/css">
	body {
      margin: 0;
      padding: 0;
      background-color: #17a2b8;
      height: 100vh;
    }
    #login .container #login-row #login-column #login-box {
      margin-top: 120px;
      max-width: 600px;
      height: 320px;
      border: 1px solid #9C9C9C;
      background-color: #EAEAEA;
    }
    #login .container #login-row #login-column #login-box #login-form {
      padding: 20px;
    }
    #login .container #login-row #login-column #login-box #login-form #register-link {
      margin-top: -85px;
    }
</style>
</head>
<body>

<div id="login">
    <div class="container">
         <div id="login-row" class="row justify-content-center align-items-center">
            <div id="login-column" class="col-md-6">
                <div id="login-box" class="col-md-12">
                    <h3 class="text-center text-info">Login</h3>
                        <form action="${pageContext.request.contextPath}/login" method="post"  >
                            <div class="form-group">
                                <label for="login" class="text-info">Login</label>
                                <input type="text" class="form-control" id="login" placeholder="Enter login" name="login" required>
                            </div>
                            <div class="form-group">
                                <label for="password" class="text-info">Password</label>
                                <input type="password" class="form-control" id="password" placeholder="Enter password" name="password" required>
                            </div>
                            <button type="submit" class="btn btn-info btn-md" >Submit</button>
                        </form>
                </div>
            </div>
         </div>
    </div>
</div>

</body>
</html>

