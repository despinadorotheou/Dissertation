<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Login</title>
    <link rel="stylesheet" type="text/css" th:href="@{/css/login.css}" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <form action="/login" method="POST" class="form-signin">
        <br/>
        <h3>Login:</h3>
        <input type="text" id="email" name="email"  th:placeholder="Email"
               class="form-control" /> <br/>
        <input type="password"  th:placeholder="Password"
               id="password" name="password" class="form-control" /> <br />
        <c:if test="${not empty param.error}">
        <div align="center" >
            <p style="font-size: 20; color: #FF1C19;">Email or Password invalid, please verify</p>
        </div>
        </c:if>
        <button class="btn btn-lg btn-primary btn-block" name="Submit" value="Login" type="Submit"> Login </button>
    </form>
</div>
</body>
</html>