<%@ page import="com.dd186.admin.Domain.Product" %>
<%@ page import="org.springframework.web.multipart.MultipartFile" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.springframework.mock.web.MockMultipartFile" %>
<!DOCTYPE HTML>
<% Product product = null;
    boolean editProduct = false;
    if (request.getAttribute("product") != null ){
        editProduct =true;
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
        <form method="POST" enctype="multipart/form-data" action="/main/add">
        <table>
            <% if (editProduct){%>
            <tr>
                <td><label for="id">Id</label></td>
                <td><input id="id" name="id" value="<%=product.getId()%>" readonly="true"/></td>
            </tr>
            <%}%>
            <tr>
                <td><label for="name">Name</label></td>
                <% if (editProduct){%>
                    <td><input id="name" name="name" value="<%=product.getName()%>" required/></td>
                <%}else{%>
                    <td><input id="name" name="name" required/></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="description">Description</label></td>
                <% if (editProduct){%>
                    <td><input id="description" name="description" value="<%=product.getDescription()%>" /></td>
                <%}else{%>
                    <td><input id="description" name="description" /></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="price">Price</label></td>
                <% if (editProduct){%>
                    <td><input id="price"  type="number" step="0.01" min="0.00" name="price"  value="<%=product.getPrice()%>" required/></td>
                <%}else{%>
                    <td><input id="price"  type="number" step="0.01" min="0.00" name="price" required/></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="quantity">Quantity</label></td>
                <% if (editProduct){%>
                    <td><input id="quantity" name="quantity" type="number" min="0" value="<%=product.getQuantity()%>" required/></td>
                <%}else{%>
                    <td><input id="quantity" type="number" min="0" name="quantity" required/></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="category">Category</label></td>
                <td>
                    <select id="category" name="category">
                        <% if (editProduct){%>
                            <c:set var="previous" value="<%=product.getCategory()%>"/>
                            <c:forEach var="category" items="${categories}">
                                <c:choose>
                                    <c:when test="${ previous == category}">
                                        <option selected value="<%=product.getCategory().getCategory()%>"><%=product.getCategory().getCategory()%></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${category.getCategory()}">${category.getCategory()}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        <%}else{%>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.getCategory()}">${category.getCategory()}</option>
                            </c:forEach>
                        <%}%>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="image">Photo</label></td>
                <td>
                    <p><input type="file" name="image" id="image"/></p>
                </td>
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