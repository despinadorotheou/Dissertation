<%@ page import="com.dd186.admin.Domain.Product" %>
<!DOCTYPE HTML>
<% Product product = null;
    boolean condition= false;
    if (request.getAttribute("product") != null ){
        condition=true;
        product = (Product) request.getAttribute("product");
    }
    %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" media="screen" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css"
      xmlns:form="http://www.w3.org/1999/xhtml">
<html>
<head>
    <title>Product Editing</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
<h2>New/edit Product Information</h2>
<form method="POST"  action="/main/add">
   <table>
       <% if (condition){%>
    <tr>
        <td><label for="id">Id</label></td>
        <td><input id="id" name="id" value="<%=product.getId()%>" readonly="true"/></td>
       <%--<%}else{%>--%>
        <%--<td><input id="id" name="id" readonly="true"/></td>--%>
    </tr>
       <%}%>
       <tr>
        <td><label for="name">Name</label></td>
       <% if (condition){%>
        <td><input id="name" name="name" value="<%=product.getName()%>"/></td>
       <%}else{%>
       <td><input id="name" name="name"/></td>
       <%}%>

    </tr>
    <tr>
        <td><label for="description">Description</label></td>
        <% if (condition){%>
        <td><input id="description" name="description" value="<%=product.getDescription()%>" /></td>
        <%}else{%>
        <td><input id="description" name="description"/></td>
        <%}%>

    </tr>
    <tr>
        <td><label for="price">Price</label></td>
        <% if (condition){%>
        <td><input id="price"  type="number" step="0.01" min="0.00" name="price"  value="<%=product.getPrice()%>" /></td>
        <%}else{%>
        <td><input id="price"  type="number" step="0.01" min="0.00" name="price"/></td>
        <%}%>
    </tr>
       <tr>
           <td><label for="quantity">Quantity</label></td>
           <% if (condition){%>
           <td><input id="quantity" name="quantity" type="number" min="0" value="<%=product.getQuantity()%> "/></td>
           <%}else{%>
           <td><input id="quantity" type="number" min="0" name="quantity"/></td>
           <%}%>
       </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Submit" class="btn btn-primary"/>
            <a class="btn btn-primary" href="/main" >Back</a>
        </td>
    </tr>
</table>
</form>
</div>
</body>
</html>