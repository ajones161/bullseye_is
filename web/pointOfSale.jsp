<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@page import="entities.User"%>
<%@page import="net.login.*"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@ page session="true" %>

<%
    User currentUser = (User) session.getAttribute("user");
    String userPerms = currentUser.getRoleID();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDateTime now = LocalDateTime.now();
    String currDate = dtf.format(now);
%>

<!DOCTYPE html>
<html xmlns:h="http://xmlns.jcp.org/jsf/html" xmlns:f="http://xmlns.jcp.org/jsf/core">
    <head>
        <title>BullsEye Inventory | Point of Sale</title>
    </head>
    <link rel="stylesheet" type="text/css" href="style/loss.css">
    <script src="js/pos.js" /></script>

<body>
    <div id="header" class="sticky">
        <input type="hidden" value="<% out.println(userPerms); %>" id="perms">
        <input type="hidden" value="<% out.println(currentUser.getUserID()); %>" id="id">
        <input type="hidden" value="<% out.println(currentUser.getLocationID());%>" id="loc">
        <div class="logo"><img src="images/bullsEye.png"></div>
        <div class="userInfo">
            Welcome, <% out.println(currentUser.getUserID()); %><br>
            Current Date: <% out.println(currDate);%>
        </div>
        <div class="logButtons">
            <button id="return">Return</button><button id="logOut">Log Out</button>
        </div>
    </div>
    <div id="content">
        <div id="panel">
            <div id="itemForm">
                <div class="formButtons">
                    <button id="returnItem">Return Item</button>
                    <button id="cashOut">Check Out</button>
                </div>
                <h2>Cart:</h2>
                <div id="cart">
                    <div id="cartTable" class="hoverable"></div>
                    <div id="total"></div>
                </div>

            </div>
            <div id="invPanel">
                <div id="searchInv">
                    Item ID: <input id="searchItemID" type="number">  
                    Item Name: <input id="searchItemName">  
                    Category: <input id="searchCategory">  
                    <button id="search">Search</button> 
                </div>
                <div id="invTable" class="hoverable"></div>
            </div>
        </div>
    </div>
</body>
</html>