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
        <title>BullsEye Web Portal</title>
    </head>
    <link rel="stylesheet" type="text/css" href="style/portal.css">
    <script src="js/portal.js" /></script>

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
            <div id="portalDeliveries">
                <div class="collapse">Today's Deliveries</div>
                <div class="content">
                    <table id="today"></table>
                    <table id="todayMobile"></table>
                </div>

                <div class="collapse">Other Deliveries</div>
                <div class="content">
                    <table id="weekly"></table>
                    <table id="weeklyMobile"></table>
                </div>
                
                <div class="collapse">Completed Deliveries</div>
                <div class="content">
                    <table id="complete"></table>
                    <table id="completeMobile"></table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>