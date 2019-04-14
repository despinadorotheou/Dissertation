<%@ page import="com.dd186.admin.Domain.Deal.Deal" %>
<!DOCTYPE html>
<html lang="en">
<% Deal deal = null;
    boolean editDeal = false;
    if (request.getAttribute("deal") != null ){
        editDeal =true;
        deal = (Deal) request.getAttribute("deal");
    }
%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="UTF-8">
    <title>Add/Edit</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <h2>New/edit Deal Information</h2>
    <form method="POST" enctype="multipart/form-data" action="/main/deals/add">
        <table>
            <% if (editDeal){%>
                <tr>
                    <td><label for="id">Id</label></td>
                    <td><input id="id" name="id" value="<%=deal.getId()%>" readonly="true"/></td>
                </tr>
            <%}%>
            <% if (editDeal){%>
            <c:set var="c" value="${1}"/>
                <%for (int i=0; i<deal.getDealCategories().size(); i++){%>
                    <c:set var="q" value="<%=deal.getDealCategories().get(i).getQuantity()%>"/>
                     <c:forEach var = "i" begin = "1" end = "${q}">
                        <tr>
                            <td><label for="category${c}">Category ${c}</label></td>
                            <td>
                                <select id="category${c}" name="category${c}">
                                    <c:set var="previous" value="<%=deal.getDealCategories().get(i).getCategory()%>"/>
                                    <c:forEach var="category" items="${categories}">
                                        <c:choose>
                                            <c:when test="${ previous == category}">
                                                <option selected value="${category.getCategory()}">${category.getCategory()}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${category.getCategory()}">${category.getCategory()}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                         <c:set var="c" value="${c+1}"/>
                    </c:forEach>
                <%}%>
            <%}else{
                for (int i=0; i<3; i++){%>
                    <tr>
                        <td><label for="category<%=i+1%>">Category <%=i+1%></label></td>
                        <td>
                            <select id="category<%=i+1%>" name="category<%=i+1%>">
                                <option hidden disabled selected value> -- select an option -- </option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.getCategory()}">${category.getCategory()}</option>
                                </c:forEach>

                            </select>
                        </td>
                    </tr>
                <%}%>
            <%}%>
            <tr>
                <td><label for="description">Description</label></td>
                <% if (editDeal){%>
                    <td><input id="description" name="description" value="<%=deal.getDescription()%>" required/></td>
                <%}else{%>
                    <td><input id="description" name="description" required/></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="value">Value</label></td>
                <% if (editDeal){%>
                    <td><input id="value" type="number" step="0.01" min="0.00" name="value" value="<%=deal.getValue()%>" required/></td>
                <%}else{%>
                    <td><input id="value" type="number" step="0.01" min="0.00" name="value" required/></td>
                <%}%>
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
                    <a class="btn btn-primary" href="/main/deals" >Back</a>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>