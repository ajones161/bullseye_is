window.onload = function () {
    userRole = document.querySelector("#perms").value.replace(/(\r\n|\n|\r)/gm, "");
    userLoc = document.querySelector("#loc").value.replace(/(\r\n|\n|\r)/gm, "");
    let d = new Date();
    date = d.getDate();
    daysOfWeek = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
    day = daysOfWeek[d.getDay()];
    document.querySelector("#logOut").addEventListener("click", logOut);
    document.querySelector("#return").addEventListener("click", returnHome);
    document.querySelector("#invTable").addEventListener("click", loadCart);

    document.querySelector("#returnItem").addEventListener("click", returnItem);
    document.querySelector("#cashOut").addEventListener("click", cashOut);

    loadData();
};

function logOut() {
    sessionStorage.clear();
    window.location.replace("index.jsp");
}

function returnHome() {
    window.location.replace("beHome.jsp");
}

function returnItem() {
    let cart = document.querySelector("#cartTable");
    let items = cart.firstChild.rows;

    for (let i = 0; i < items.length; i++) {
        buildLoss();
        sendReturn(items[i].id, items[i].cells[1].innerText);
    }
    
    cart.innerHTML = "";
    document.querySelector("#total").innerHTML = "";
}

function buildLoss() {
    url = "OrdServlet/Orders/Update/Loss/" + userLoc;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                loadData();
            }
        }
    };
    xmlhttp.open("PUT", url, true);
    xmlhttp.send();
}

function sendReturn(id, quant) {
    let url = "OrdServlet/Orders/Update/FillLoss/" + id + "/return/" + quant + "/" + userLoc;

    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                loadData();
            }
        }
    };
    xmlhttp.open("PUT", url, true);
    xmlhttp.send();
}

function cashOut() {
    let cart = document.querySelector("#cartTable");
    let items = cart.firstChild.rows;
    buildOrder();
    for (let i = 0; i < items.length; i++) {
        sendOrder(items[i].id, items[i].cells[1].innerText);
    }
    cart.innerHTML = "";
    document.querySelector("#total").innerHTML = "";
}

function buildOrder() {
    let url = "InvServlet/invItems/create/" + userLoc;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                loadData();
            }
        }
    };
    xmlhttp.open("PUT", url, true);
    xmlhttp.send();
}

function sendOrder(itemID, quant) {
    let loc = userLoc;
    let url = "InvServlet/invItems/fillSale/" + loc + "/" + itemID + "/" + quant;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                loadData();
            }
        }
    };
    xmlhttp.open("PUT", url, true);
    xmlhttp.send();
}


function loadData() {
    url = "InvServlet/invItems/pointOfSale/" + userLoc;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                buildTable(response);
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function buildTable(resp) {
    let viewScreen = document.querySelector("#invTable");
    viewScreen.innerHTML = "";
    let headers = ["ID", "Item Name", "Description", "Category", "In Stock", "Price"];
    let table = "<table id='inv'><thead><tr>";
    Object.keys(headers).forEach(function (item) {
        table += "<th>" + headers[item] + "</th>";
    });
    let arr = JSON.parse(resp);
    let inner = "</tr></thead><tbody>";
    let zeroQ = "";
    for (let i = 0; i < arr.length; i++) {
        let row = arr[i];
        if (row.quantity !== 0) {
            zeroQ += "<tr>" + fillRow(row) + "</tr>";
        }
    }
    inner += zeroQ;
    table += inner;
    table += "</tbody></table>";
    viewScreen.innerHTML = table;
}

function fillRow(row) {
    let fill = "";
    fill += "<td><div>" + row.itemID + "</div></td>";
    fill += "<td><div>" + row.itemName + "</div></td>";
    fill += "<td><div>" + row.description + "</div></td>";
    fill += "<td><div>" + row.categoryName + "</div></td>";
    fill += "<td><div>" + row.quantity + "</div></td>";
    fill += "<td><div>" + row.retailPrice + "</div></td>";
    return fill;
}

function searchTable() {
    let table, tr, td, i;
    let itemID = document.querySelector("#searchItemID").value;
    let itemName = document.querySelector("#searchItemName").value.toUpperCase();
    let category = document.querySelector("#searchCategory").value.toUpperCase();

    table = document.getElementById("inv");
    tr = table.getElementsByTagName("tr");
    for (let t = 1; t < tr.length; t++) {
        tr[t].style.display = "";
    }

    for (i = 1; i < tr.length; i++) {
        td = tr[i].cells;
        let id = td[0].textContent || td[0].innerText;
        let name = td[1].textContent || td[1].innerText;
        let cat = td[2].textContent || td[2].innerText;


        if (itemID !== "") {
            if (id.indexOf(itemID) > -1) {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "";
                }
            } else {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "none";
                }
            }
        }

        if (itemName !== "") {
            if (name.toUpperCase().indexOf(itemName) > -1) {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "";
                }
            } else {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "none";
                }
            }
        }

        if (category !== "") {
            if (cat.toUpperCase().indexOf(category) > -1) {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "";
                }
            } else {
                if (tr[i].style.display !== "none") {
                    tr[i].style.display = "none";
                }
            }
        }
    }
}

function loadCart(e) {
    let target = e.target.parentElement.parentElement;
    let cells = target.cells;
    let cart = document.querySelector("#cartTable");
    let total = 0;
    let inner = "<table><tbody>";

    if (cart.innerHTML !== "") {
        let temp;
        let row;
        let bool = false;
        //get all other stuff in cart
        temp = cart.firstElementChild.rows;
        for (let i = 0; i < temp.length; i++) {
            inner += "<tr id = '" + temp[i].id + "'>";
            inner += temp[i].cells[0].outerHTML;
            if (temp[i].id === cells[0].innerText) {
                let quant = Number(temp[i].children[1].innerText) + 1;
                inner += "<td><div>" + quant + "</div></td>";
                total += quant * Number(temp[i].cells[2].innerText);
                bool = true;
            } else {
                inner += temp[i].cells[1].outerHTML;
                total += Number(temp[i].cells[2].innerText) * Number(temp[i].cells[1].innerText);
            }
            inner += temp[i].cells[2].outerHTML;


            inner += "</tr>";
        }
        if (!bool) {
            inner += "<tr id='" + cells[0].innerText + "'>";
            inner += "<td><div>" + cells[1].innerText + "</div></td>";
            inner += "<td><div>" + 1 + "</div></td>";
            inner += "<td><div>" + cells[5].innerText + "</div></td>";
            inner += "</tr>";
            total += Number(cells[5].innerText);
        }
    } else {
        inner += "<tr id='" + cells[0].innerText + "'>";
        inner += "<td><div>" + cells[1].innerText + "</div></td>";
        inner += "<td><div>" + 1 + "</div></td>";
        inner += "<td><div>" + cells[5].innerText + "</div></td>";
        inner += "</tr>";
        total += Number(cells[5].innerText);
    }

    inner += "</tbody></table>";

    cart.innerHTML = inner;

    console.log(total);

    fillTotal(total);
}

function fillTotal(total) {
    let tax = Number(total) * 0.15;
    let befTotal = (Math.round(total * 100) / 100).toFixed(2);

    let finTotal = Number(total) + tax;
    let fill = document.querySelector("#total");
    let filler = "Before Tax: " + befTotal;
    filler += "<br>HST: " + (Math.round(tax * 100) / 100).toFixed(2);
    filler += "<br>TOTAL: " + (Math.round(finTotal * 100) / 100).toFixed(2);

    fill.innerHTML = filler;
}
