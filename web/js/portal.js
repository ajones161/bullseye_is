window.onload = function () {
    userRole = document.querySelector("#perms").value.replace(/(\r\n|\n|\r)/gm, "");
    userLoc = document.querySelector("#loc").value.replace(/(\r\n|\n|\r)/gm, "");
    let d = new Date();
    date = d.getDate();
    daysOfWeek = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
    day = daysOfWeek[d.getDay()];
    document.querySelector("#logOut").addEventListener("click", logOut);
    document.querySelector("#return").addEventListener("click", returnHome);

    loadDeliveries();

};

function returnHome() {
    window.location.replace("beHome.jsp");
}

function loadDeliveries() {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                buildTables(response);
            }
        }
    };
    xmlhttp.open("GET", "DelServlet/Deliveries/", true);
    xmlhttp.send();
}

function buildTables(data) {
    let today = document.querySelector("#today");
    let later = document.querySelector("#weekly");
    let todayMobile = document.querySelector("#todayMobile");
    let laterMobile = document.querySelector("#weeklyMobile");
    let complete = document.querySelector("#complete");
    let completeMobile = document.querySelector("#completeMobile");
    let arr = JSON.parse(data);

    let tableHead = "<thead><tr><th>ID</th><th>Courier</th><th>Address</th><th>Route</th><th>Distance (KM)</th><th>Vehicle Type</th><th>Start Date</th><th>Delivery Date</th>";
    let mobileHead = "<thead><tr><th>Address</th><th>Route</th><th>Vehicle</th><th>Start Date</th><th>Delivery Date</th>";
    let innerTD = "</tr></thead><tbody>";
    let innerL8 = "</tr></thead><tbody>";
    let innerComp = "</tr></thead><tbody>";
    let innerTDM = "</tr></thead><tbody>";
    let innerL8M = "</tr></thead><tbody>";
    let innerCM = "</tr></thead><tbody>";
    let counter = 0;
    let counter2 = 0;
    let counter3 = 0;

    for (let i = 0; i < arr.length; i++) {
        let row = arr[i];
        let inputDate = new Date(arr[i].deliveryDay);
        let todaysDate = new Date();

        if (row.orderStatus === "COMPLETE" || row.orderStatus === "DELIVERED") {
            innerComp += "<tr>";
            innerComp += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerComp += "<td>" + row.deliveryID + "</td>";
            innerComp += "<td>" + row.courierName + "</td>";
            innerComp += "<td>" + row.address + "</td>";
            innerComp += "<td>" + row.routeID + "</td>";
            innerComp += "<td>" + row.distance + "</td>";
            innerComp += "<td>" + row.vehicleID + "</td>";
            innerComp += "<td>" + row.placementDay + "</td>";
            innerComp += "<td>" + row.deliveryDay + "</td>";
            innerComp += "</tr>";
            innerCM += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerCM += "<td>" + row.address + "</td>";
            innerCM += "<td>" + row.routeID + "</td>";
            innerCM += "<td>" + row.vehicleID + "</td>";
            innerCM += "<td>" + row.placementDay + "</td>";
            innerCM += "<td>" + row.deliveryDay + "</td>";
            innerCM += "</tr>";
            counter3++;
        } else if (inputDate.setHours(0, 0, 0, 0) === todaysDate.setHours(0, 0, 0, 0)) {
            innerTD += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerTD += "<td>" + row.deliveryID + "</td>";
            innerTD += "<td>" + row.courierName + "</td>";
            innerTD += "<td>" + row.address + "</td>";
            innerTD += "<td>" + row.routeID + "</td>";
            innerTD += "<td>" + row.distance + "</td>";
            innerTD += "<td>" + row.vehicleID + "</td>";
            innerTD += "<td>" + row.placementDay + "</td>";
            innerTD += "<td>" + row.deliveryDay + "</td>";
            innerTD += "<td>" + "<button class='loadOrd'>Sign For Order</button>" + "</td>";
            innerTD += "</tr>";
            innerTDM += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerTDM += "<td>" + row.address + "</td>";
            innerTDM += "<td>" + row.routeID + "</td>";
            innerTDM += "<td>" + row.vehicleID + "</td>";
            innerTDM += "<td>" + row.placementDay + "</td>";
            innerTDM += "<td>" + row.deliveryDay + "</td>";
            innerTDM += "<td>" + "<button class='loadOrd'>Sign</button>" + "</td>";
            innerTDM += "</tr>";
            counter++;
        } else {
            innerL8 += "<tr>";
            innerL8 += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerL8 += "<td>" + row.deliveryID + "</td>";
            innerL8 += "<td>" + row.courierName + "</td>";
            innerL8 += "<td>" + row.address + "</td>";
            innerL8 += "<td>" + row.routeID + "</td>";
            innerL8 += "<td>" + row.distance + "</td>";
            innerL8 += "<td>" + row.vehicleID + "</td>";
            innerL8 += "<td>" + row.placementDay + "</td>";
            innerL8 += "<td>" + row.deliveryDay + "</td>";
            innerL8 += "</tr>";
            innerL8M += "<tr id='" + row.deliveryID + "' class=' " + row.orderStatus + "'>";
            innerL8M += "<td>" + row.address + "</td>";
            innerL8M += "<td>" + row.routeID + "</td>";
            innerL8M += "<td>" + row.vehicleID + "</td>";
            innerL8M += "<td>" + row.placementDay + "</td>";
            innerL8M += "<td>" + row.deliveryDay + "</td>";
            innerL8M += "</tr>";
            counter2++;
        }
    }

    innerTD += "</tbody>";
    innerL8 += "</tbody>";
    if (counter === 0) {
        innerTD = "";
    } else {
        today.innerHTML = tableHead + "<th>Load Order</th></tr></thead>" + innerTD;
        todayMobile.innerHTML = mobileHead + "<th>Load</th></tr></thead>" + innerTDM;
        document.querySelector(".loadOrd").addEventListener("click", signOrder);
    }
    if (counter2 === 0) {
        innerL8 = "";
    } else {
        later.innerHTML = tableHead + "</tr></thead>" + innerL8;
        laterMobile.innerHTML = mobileHead + "</tr></thead>" + innerL8M;
    }
    if (counter3 === 0) {
        innerComp = "";
    } else {
        complete.innerHTML = tableHead + "</tr></thead>" + innerComp;
        completeMobile.innerHTML = mobileHead + "</tr></thead>" + innerCM;
    }

}

function logOut() {
    sessionStorage.clear();
    window.location.replace("index.jsp");
}

function signOrder(e) {
    let orderID = e.target.offsetParent.parentNode.id;
    let row = e.target.offsetParent.parentNode;
    let lctn = (row.cells[3].innerText.split('-'))[1];
    if (row.classList.contains("READY")) {
        if (lctn === userLoc || userRole === "Admin"  || userRole === "Warehouse Manager") {
            signDelivery(orderID);
        } else {
            alert("Cannot sign for this order.");
        }
    } else if (row.classList.contains("TRANSIT")) {
        if (lctn === userLoc || userRole === "Admin" || userRole === "Warehouse Manager") {
            signDelivery(orderID);
        } else {
            alert("Cannot sign for this order.");
        }
    } else {
        alert("Cannot load this order.");
    }

}

function signDelivery(id) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                location.reload();
            }
        }
    };
    xmlhttp.open("PUT", "DelServlet/Deliveries/" + id, true);
    xmlhttp.send();
}