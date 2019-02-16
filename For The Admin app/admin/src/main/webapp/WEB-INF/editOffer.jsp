<%@ page import="com.dd186.admin.Domain.Offer" %>
<!DOCTYPE html>
<html lang="en">
<% Offer offer = null;
    boolean editOffer = false;
    if (request.getAttribute("offer") != null ){
        editOffer =true;
        offer = (Offer) request.getAttribute("offer");
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
    <form method="POST" action="/main/offers/add">
        <table>
            <% if (editOffer){%>
            <tr>
                <td><label for="id">Id</label></td>
                <td><input id="id" name="id" value="<%=offer.getId()%>" readonly="true"/></td>
            </tr>
            <%}%>
            <% if (editOffer){
                for (int i=0; i<offer.getOfferProducts().size(); i++){%>
            <tr>
                <td><label for="product<%=i+1%>">Product <%=i+1%></label></td>
                <td>
                    <select id="product<%=i+1%>" name="product<%=i+1%>">
                        <c:set var="previous" value="<%=offer.getOfferProducts().get(i).getProduct()%>"/>
                        <c:forEach var="offerProduct" items="${offer.getOfferProducts()}">
                            <c:choose>
                                <c:when test="${ previous == offerProduct.getProduct()}">
                                    <option selected value="${offerProduct.getProduct().getId()}">${offerProduct.getProduct().getName()}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${offerProduct.getProduct().getId()}">${offerProduct.getProduct().getName()}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <%}%>
            <%}else{
                for (int i=0; i<3; i++){%>
            <tr>
                <td><label for="product<%=i+1%>">Product <%=i+1%></label></td>
                <td>
                    <select id="product<%=i+1%>" name="product<%=i+1%>">
                        <option hidden disabled selected value> -- select an option -- </option>
                        <c:forEach var="product" items="${products}">
                            <option value="${product.getId()}">${product.getName()}</option>
                        </c:forEach>

                    </select>
                </td>
            </tr>
            <%}%>
            <%}%>
            <tr>
                <td><label for="description">Description</label></td>
                <% if (editOffer){%>
                <td><input id="description" name="description" value="<%=offer.getDescription()%>" required/></td>
                <%}else{%>
                <td><input id="description" name="description" required/></td>
                <%}%>
            </tr>
            <tr>
                <td><label for="value">Value</label></td>
                <% if (editOffer){%>
                <td><input id="value" type="number" step="0.01" min="0.00" name="value" value="<%=offer.getValue()%>" required/></td>
                <%}else{%>
                <td><input id="value" type="number" step="0.01" min="0.00" name="value" required/></td>
                <%}%>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Submit" class="btn btn-primary"/>
                    <a class="btn btn-primary" href="/main/offers" >Back</a>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>