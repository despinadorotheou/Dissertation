<%@ page import="com.dd186.admin.Domain.Product" %>
<%@ page import="java.util.List" %>
<!DOCTYPE HTML>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String mes = (String) request.getAttribute("userName"); %>
<html>
<head>
    <title>Main</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
<h2><%=mes%></h2>
<h2>Product Information</h2>
    <br>
    <div class="input-group mb-3">
        <div class="input-group-prepend">
            <select class="custom-select" id="select">
                <option selected value="1">Name</option>
                <option value="0">ID</option>
                <option value="2">Description</option>
                <option value="3">Price</option>
                <option value="4">Quantity</option>
            </select>
        </div>
        <input type="text" id="searchValue" class="form-control" onkeyup="searchFunction()" placeholder="Search...">
    </div>+
<section>
    <a class="btn btn-primary" href="/main/edit">Add new product</a>
</section>
    <br>
<section>
    <div class="table table-responsive-md">
    <table id="table" class="table table-hover">
        <thead>
        <tr>
            <td><h3>Id</h3></td>
            <td><h3>Name</h3></td>
            <td><h3>Description</h3></td>
            <td><h3>Price</h3></td>
            <td><h3>Quantity</h3></td>
            <td><h3></h3></td>
            <td><h3></h3></td>
        </tr>
        </thead>
        <c:forEach items="${products}" var="product">
            <tr>
                <td><c:out value="${product.getId()}"/></td>
                <td><c:out value="${product.getName()}"/></td>
                <td><c:out value="${product.getDescription()}"/></td>
                <td><c:out value="${product.getPrice()}"/></td>
                <td><c:out value="${product.getQuantity()}"/></td>
                <td><a class="btn btn-success" href="/main/edit?productId=${product.getId()}">Edit</a></td>
                <td><a class="btn btn-danger" href="/main/delete?productId=${product.getId()}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
    </div>
</section>
</div>
<script type="text/javascript">
    function searchFunction() {
        var searchKeyWord = document.getElementById("searchValue");
        var filter= searchKeyWord.value.toUpperCase();
        var table = document.getElementById("table");
        var tr = table.getElementsByTagName("tr");


        for ( var i=0; i<tr.length; i++){
            var td = tr[i].getElementsByTagName("td")[ $("#select").val()];
            if (td){
                var columnVal = td.textContent || td.innerHTML;
                if(columnVal.toUpperCase().indexOf(filter)>-1){
                    tr[i].style.display= "";
                } else {
                    tr[i].style.display= "none";

                }
            }
        }
    }
</script>
</body>
</html>
