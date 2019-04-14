<%@ page import="com.dd186.admin.Domain.Order.OrderStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="5">
    <title>Orders</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <h2>Orders</h2>
    <br>
    <div class="input-group mb-3">
        <div class="input-group-prepend">
            <select class="custom-select" id="select">
                <option selected value="0">ID</option>
                <%--<option value="0">ID</option>--%>

            </select>
        </div>
        <input type="text" id="searchValue" class="form-control" onkeyup="searchFunction()" placeholder="Search...">
    </div>
    <section>
        <a class="btn btn-primary" href="/main/products">Go to the products</a>
        <a class="btn btn-primary" href="/main/deals">Go to the deals</a>
        <a class="btn btn-primary" href="/main/offers">Go to the offers</a>
    </section>
    <br>
    <section>
        <div class="table table-responsive-md">
            <table id="table" class="table table-hover">
                <thead>
                <tr>
                    <td><h3>Order ID</h3></td>
                    <td><h3>Products ID</h3></td>
                    <td><h3>Products in Order</h3></td>
                    <td><h3>Quantity</h3></td>
                    <td><h3>Price</h3></td>
                    <td><h3>Paid</h3></td>
                    <td><h3>Status</h3></td>
                    <td><h3></h3></td>
                    <td><h3></h3></td>
                </tr>
                </thead>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td><c:out value="${order.getId()}"/></td>
                        <td>
                            <p>
                                <c:forEach items="${order.getOrderProducts()}" var="product">
                                    <c:out value="${product.getProduct().getId()}"/> <br>
                                </c:forEach>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:forEach items="${order.getOrderProducts()}" var="product">
                                    <c:out value="${product.getProduct().getName()}"/> <br>
                                </c:forEach>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:forEach items="${order.getOrderProducts()}" var="product">
                                    <c:out value="${product.getQuantity()}"/> <br>
                                </c:forEach>
                            </p>
                        </td>
                        <td><c:out value="${order.getValue()}"/></td>
                        <c:if test="${order.isPaid()}">
                            <td><c:out value="Yes"/></td>
                        </c:if>
                        <c:if test="${!order.isPaid()}">
                            <td><c:out value="No"/></td>
                        </c:if>
                        <td><c:out value="${order.getStatus()}"/></td>
                        <c:set var="pending" value="<%=OrderStatus.PENDING%>"/>
                        <c:if test="${order.getStatus() == pending}">
                            <td><a class="btn btn-warning" href="/main/order/ready?orderId=${order.getId()}">READY</a></td>
                        </c:if>
                        <c:set var="ready" value="<%=OrderStatus.READY%>"/>
                        <c:if test="${order.getStatus() == ready}">
                            <td><a class="btn btn-success" href="/main/order/collected?orderId=${order.getId()}">COLLECTED</a></td>
                        </c:if>
                        <c:set var="editing" value="<%=OrderStatus.EDITING%>"/>
                        <c:if test="${order.getStatus() == editing}">
                            <td><a class="btn btn-warning" style="pointer-events: none; cursor: default;" href="/main/order/ready?orderId=${order.getId()}">READY</a></td>
                        </c:if>
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