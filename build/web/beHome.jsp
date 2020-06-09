<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@page import="entities.User"%>
<%@page import="net.login.*"%>
<%@ page session="true" %>

<%
    User currentUser = (User) session.getAttribute("user");
    String userPerms = currentUser.getRoleID();
%>

<!DOCTYPE html>
<html xmlns:h="http://xmlns.jcp.org/jsf/html" xmlns:f="http://xmlns.jcp.org/jsf/core">
    <head>
        <title>BullsEye Inventory</title>
    </head>
    <link rel="stylesheet" type="text/css" href="style/mainStyle.css">
    <script src="js/main.js" /></script>

<body>
    <div id="header" class="sticky">
        <div class="logo"><img src="images/bullsEye.png"></div>
        <div class="userInfo">
            Welcome, <% out.println(currentUser.getUserID()); %><br>
            Current Location: <% out.println(currentUser.getLocationID());%>
        </div>
        <div class="logButtons">
            <a href="/BullsEye_IS/pointOfSale.jsp">Point Of Sale</a>
            <div id="burg"><button id="openSide">open</button></div>
            <div id="top"><button id="inventory">Inventory</button><button id="order">Orders</button><button id="report">Reports</button><button id="items">Items</button><button id="locations">Locations</button><button id="couriers">Couriers</button><button id="users">Users</button></div>
            <button id="logOut">Log Out</button>
        </div>
    </div>
    <div id="content">
        <div id="panel">
            <div id="splash" class="screen">
                <input type="hidden" value="<% out.println(userPerms); %>" id="perms">
                <input type="hidden" value="<% out.println(currentUser.getUserID()); %>" id="id">
                <input type="hidden" value="<% out.println(currentUser.getLocationID());%>" id="loc">
            </div>
            <!-- inventory view -->
            <div id="inventoryView" class="screen">
                <div id="invPanel" class="inv">
                    <input id="invSearch">
                    <button type="button" id="searchBtn">Search</button>

                    <div id="switcher"><label>Select Location:</label><select class="locID" id="invLocation"></select></div> 

                    <button id="upd8Btn" disabled>Update Quantities</button> 
                    <button id="loss">Loss/Returns/Damage</button> 
                    <div id="invUpdate" class="update inventory hidden">
                        <div>
                            <input type="hidden" value="" class="inInv">
                            <input type="hidden" value="" class="inInv">
                            <label>Reorder Threshold: </label><input type="number" min="0" class="inInv"><br>
                            <label>Reorder Level: </label><input type="number" min="0" class="inInv"><br>
                            <label>Quantity: </label><input type="number" min="0" class="inInv"><br>
                        </div>
                        <button id="submitCrud">Submit</button>
                        <button id="cancel">Cancel</button>

                    </div>
                </div>
                <div id="invTable">

                </div>
            </div>
            <!-- order view -->
            <div id="orderView" class="screen">
                <div id="orderFrame">
                    <div id="topHalf">
                        <div class="selectContain">
                            Select Order
                            <select class="orderList" name="orders" size="10">

                            </select>
                            Filter By Location
                            <select class="locID" id="ordLocation">

                            </select>
                        </div>
                        <div class="shippingInfo">

                        </div>
                        <div class="notes">

                        </div>
                        <div class="buttons">
                            <button id="portal">Deliveries</button>
                            <button id = "editOrder">Edit Item</button><br>
                            <button id = "startEmerg">Create Emergency Order</button><br>
                            <button id = "createOrder">Manual New Order</button><br>
                            <button id = "nextStatus">Next Status</button><br>
                            <button id = "returnStore">Return to Store</button>
                        </div>
                    </div>
                    <div id="bottomHalf">
                        <div id="ordTable" >

                        </div>
                    </div>
                </div>
            </div>
            <!-- report view -->
            <div id="reportView" class="screen">
                empty. see dossier.

            </div>
            <!-- item view -->
            <div id="itemsView" class="screen">
                <div id="itmPanel" class="itm">
                    <input id="itmSearch">
                    <button type="button" id="searchBtn">Search</button>
                    <button id="add2Order" disabled>Add To Open Order</button> <button id="add2Emergency" disabled>Add To Emergency Order</button> <button id="addBtn">Add New</button> <button id="upd8Btn" disabled>Update</button>
                    <div id="itmUpdate" class="update items hidden">
                        <div class="float">
                            <label>ID:</label><input type="number" min="0" class="inItm"><br>
                            <label>Name:</label><input type="text" class="inItm"><br>
                            <label>SKU:</label><input type="number" class="inItm"><br>
                            <label>Description:</label><input type="text" class="inItm"><br>

                            <label>Category:</label><select class="category inItm"></select><br>
                            <label>Weight:</label><input  type="number" step="0.01" min="0" class="inItm"><br>
                            <label>Cost Price:</label><input type="number" step="0.01" min="0" class="inItm"><br>
                        </div>
                        <div>
                            <label>Retail Price:</label><input type="number" step="0.01" min="0" class="inItm"><br>
                            <label>Supplier:</label><select class="supplier inItm"></select><br>
                            <label>Case Size:</label><input type="number" class="inItm"><br>
                            <label>Notes:</label><input type="text" class="inItm"><br>
                            <label>Active?</label><input type="checkbox" class="inItm">
                        </div>
                        <button id="submitCrud">Submit</button>
                        <button id="cancel">Cancel</button>
                    </div>
                </div>
                <div id="itmTable" >

                </div>
            </div>
            <!-- location view -->
            <div id="locationsView" class="screen">
                <div id="locPanel" class="loc">
                    <input id="locSearch">
                    <button type="button" id="searchBtn">Search</button>
                    <button id="addBtn">Add New</button> <button id="upd8Btn" disabled>Update</button>
                    <div id="locUpdate" class="update locations hidden">
                        <div class="float">
                            <label>Location ID</label><input type="text" class="inLoc"><br>
                            <label>Description:</label><input type="text" class="inLoc"><br>
                            <label>Address:</label><input type="text" class="inLoc"><br>
                            <label>City:</label><input type="text" class="inLoc"><br>
                            <label>Province:</label><select class="province inLoc"></select><br>
                            <label>Postal Code:</label><input type="text" class="inLoc"><br>
                            <label>Country:</label><select class="inLoc">
                                <option value="Canada">Canada</option>
                                <option value="United States">United States</option>
                            </select><br>
                        </div>
                        <div>
                            <label>Type:</label><select class="lType inLoc"></select><br>
                            <label>Delivery Day:</label><select class="inLoc">
                                <option value="">None</option>
                                <option value="MONDAY">Monday</option>
                                <option value="TUESDAY">Tuesday</option>
                                <option value="WEDNESDAY">Wednesday</option>
                                <option value="THURSDAY">Thursday</option>
                                <option value="FRIDAY">Friday</option>
                                <option value="ANY">Any</option>
                            </select><br>
                            <label>Active?</label><input type="checkbox" class="inLoc">
                            <br>
                        </div>
                        <button id="submitCrud">Submit</button>
                        <button id="cancel">Cancel</button>
                    </div>
                </div>
                <div id="locTable">

                </div>
            </div>
            <!-- courier view -->
            <div id="couriersView" class="screen">
                <div id="couPanel" class="cou">
                    <input id="couSearch">
                    <button id="searchBtn">Search</button>
                    <button id="addBtn">Add New</button> <button id="upd8Btn" disabled>Update</button>
                    <div id="couUpdate" class="update courier hidden">
                        <div class="float">
                            <label>ID</label><input type="text" class="inCou" disabled><br>
                            <label>Company</label><input type="text" class="inCou"><br>
                            <label>Address:</label><input type="text" class="inCou"><br>
                            <label>City:</label><input type="text" class="inCou"><br>
                            <label>Province:</label><select class="province inCou"></select><br>
                            <label>Postal Code:</label><input type="text" class="inCou"><br>
                            <label>Country:</label><select class="inCou">
                                <option value="Canada">Canada</option>
                                <option value="United States">United States</option>
                            </select><br>
                        </div>
                        <div>
                            <label>Email:</label><input type="text" class="inCou"><br>
                            <label>Phone:</label><input type="text" class="inCou"><br>
                            <label>Notes:</label><input type="text" class="inCou">
                            <label>Active?</label><input type="checkbox" class="inCou">
                        </div>
                        <button id="submitCrud">Submit</button>
                        <button id="cancel">Cancel</button>
                    </div>
                </div>
                <div id="couTable"></div>
            </div>
            <!-- user view -->
            <div id="usersView" class="screen">
                <div id="usrPanel" class="usr">
                    <input id="usrSearch">
                    <button type="button" id="searchBtn">Search</button>
                    <button id="addBtn">Add New</button> <button id="upd8Btn" disabled>Update</button>
                </div>
                <div id="usrTable"></div>
                <div id="usrUpdate" class="user">

                    <label>User ID:</label><input type="text" class="inUsr" disabled><br>
                    <label>Location:</label><select class="locID inUsr" disabled></select><br>
                    <label>Role:</label><select class="role inUsr" disabled></select><br>
                    <label>Password:</label><input type="text" class="inUsr" disabled> <i>Leave blank if not changing.</i><br>
                    <label>Active?</label><input type="checkbox" class="inUsr"><br>
                    <button id="submitCrud" class="inUsr" disabled>Submit</button>
                    <button id="cancel" class="inUsr" disabled>Cancel</button>

                </div>
            </div>
        </div>
    </div>
    <div id="mySidenav" class="sidenav">
        <div id="sidebnav">
            <a href="" class="closebtn">&times;</a>
            <button id="inventory">Inventory</button>
            <button id="order">Orders</button>
            <button id="report">Reports</button>
            <button id="items">Items</button>
            <button id="locations">Locations</button>
            <button id="couriers">Couriers</button>
            <button id="users">Users</button>
        </div>
    </div>
</body>
</html>