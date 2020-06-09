<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page session="true" %>
<%
    String error = " ";
    if ((String) session.getAttribute("Error") != null) {
        error = (String) session.getAttribute("Error");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BullsEye Inventory | Login</title>
        <link rel="stylesheet" type="text/css" href="style/loginStyle.css">
        <script src="js/login.js" /></script>
</head>
<body>
    <div id="header">

    </div>
    <div id="content">
        <div id="panel">
            <div class="title"><div class="logo"><img src="images/bullsEye.png"></div> Enter your login details:</div>
            <form action="<%=request.getContextPath()%>/login" method="post">
                <p><label>Username</label><input name="username" id="un"></p>
                <p><label>Password</label><input type="password" id="pw" name="password">
                <p2><input type="checkbox" id="checkbox"><label for="checkbox">Show Password</label></p2>
                <button type="submit">Login</button>
                <div class="error"><% out.println(error);%></div>
            </form>
        </div>
    </div>

</body>
</html>
