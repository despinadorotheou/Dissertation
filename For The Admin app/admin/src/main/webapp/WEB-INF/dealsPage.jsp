<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Deals</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <h2>Deal Information</h2>
    <br>
    <div class="input-group mb-3">
        <div class="input-group-prepend">
            <select class="custom-select" id="select">
                <option selected value="0">ID</option>
            </select>
        </div>
        <input type="text" id="searchValue" class="form-control" onkeyup="searchFunction()" placeholder="Search...">
    </div>
    <section>
        <a class="btn btn-primary" href="/main">Back to Main</a>
        <a class="btn btn-primary" href="/main/deals/edit">Add new deal</a>
    </section>
    <br>
    <section>
        <div class="table table-responsive-md">
            <table id="table" class="table table-hover">
                <thead>
                <tr>
                    <td><h3>Id</h3></td>
                    <td><h3>Categories in the Deal</h3></td>
                    <td><h3>Quantity</h3></td>
                    <td><h3>Description</h3></td>
                    <td><h3>Value</h3></td>
                    <td><h3>Image</h3></td>
                    <td><h3></h3></td>
                    <td><h3></h3></td>
                </tr>
                </thead>
                <c:forEach items="${deals}" var="deal">
                    <tr>
                        <td><c:out value="${deal.getId()}"/></td>
                        <td>
                            <p>
                                <c:forEach items="${deal.getDealCategories()}" var="category">
                                    <c:out value="${category.getCategory().getCategory()}"/> <br>
                                </c:forEach>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:forEach items="${deal.getDealCategories()}" var="category">
                                    <c:out value="${category.getQuantity()}"/> <br>
                                </c:forEach>
                            </p>
                        </td>
                        <td><c:out value="${deal.getDescription()}"/></td>
                        <td><c:out value="${deal.getValue()}"/></td>
                        <c:if test="${deal.getImage() != null}">
                            <td><a class="btn btn-primary" href="/main/deals/image?dealId=${deal.getId()}">See Image</a></td>
                        </c:if>
                        <c:if test="${deal.getImage() == null}">
                            <td><c:out value="No Image"/></td>
                        </c:if>
                        <td><a class="btn btn-success" href="/main/deals/edit?dealId=${deal.getId()}">Edit</a></td>
                        <td><a class="btn btn-danger" href="/main/deals/delete?dealId=${deal.getId()}">Delete</a></td>
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