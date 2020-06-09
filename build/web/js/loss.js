let txn = "";
let enabled = false;

window.onload = function () {
    userRole = document.querySelector("#perms").value.replace(/(\r\n|\n|\r)/gm, "");
    userLoc = document.querySelector("#loc").value.replace(/(\r\n|\n|\r)/gm, "");
    let d = new Date();
    date = d.getDate();
    daysOfWeek = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
    day = daysOfWeek[d.getDay()];
    document.querySelector("#logOut").addEventListener("click", logOut);
    document.querySelector("#return").addEventListener("click", returnHome);

    document.querySelector("#btnLoss").addEventListener("click", createLoss);
    document.querySelector("#btnReturn").addEventListener("click", createLoss);
    document.querySelector("#btnDamage").addEventListener("click", createLoss);

    document.querySelector("#submit").addEventListener("click", submitLoss);
    document.querySelector("#invTable").addEventListener("click", loadCells);
    document.querySelector("#search").addEventListener("click", searchTable);
    loadData();

};

function createLoss(e) {
    switch (e.target.id) {
        case "btnLoss":
            txn = "Loss";
            break;
        case "btnReturn":
            txn = "Return";
            break;
        case "btnDamage":
            txn = "Damage";
            break;
        default:
            break;
    }

    let r = confirm("Create a " + txn.toLowerCase() + " report?");
    if (r == true) {
        enabled = true;
        document.querySelector("#invTable").classList.remove("faded");
        document.querySelector("#invTable").classList.add("hoverable");
        if(txn !== "Return" && txn !== "Damage") {
            doLoss(0);
        }
    }
}

function submitLoss() {
    enabled = true;
    document.querySelector("#invTable").classList.add("faded");
    document.querySelector("#invTable").classList.remove("hoverable");
    if (txn === "Loss") {
        doLoss(1);
    } else if (txn === "Return" || txn === "Damage") {
        doLoss(2);
    }

}

function doLoss(y) {
    let itemID = document.querySelector("#itemID").value;
    let notes = document.querySelector("#notes").value;
    let amount = document.querySelector("#returnQuant").value;
    
    let act = "";
    
    if (y === 0) {
        url = "OrdServlet/Orders/Update/Loss/" + userLoc;
    } else if (y === 1) {
        url = "OrdServlet/Orders/Update/FillLoss/" + itemID + "/" + notes + "/" + amount + "/" + userLoc;
    } else if (y === 2) {
        let transType = txn.toUpperCase();
        url = "OrdServlet/Orders/Update/CreateReturn/" + userLoc + "/" + itemID + "/" + amount + "/" + transType;
    }

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

function loadCells(e) {
    let target = e.target.parentElement.parentElement;
    if (enabled) {
        let cells = target.cells;
        let formInputs = document.querySelectorAll(".frm");

        for (let i = 0; i < formInputs.length; i++) {
            formInputs[i].value = cells[i].innerText;
        }

    }
}


function logOut() {
    sessionStorage.clear();
    window.location.replace("index.jsp");
}

function returnHome() {
    window.location.replace("beHome.jsp");
}

function loadData() {
    url = "InvServlet/invItems/" + userLoc;
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
    let headers = ["ID", "Item Name", "Category", "SKU", "Supplier", "In Stock"];
    let table = "<table id='inv'><thead><tr>";
    Object.keys(headers).forEach(function (item) {
        table += "<th>" + headers[item] + "</th>";
    });
    let arr = JSON.parse(resp);
    let inner = "</tr></thead><tbody>";
    let zeroQ = "", oneQ = "", twoQ = "";
    for (let i = 0; i < arr.length; i++) {
        let row = arr[i];
        if (row.quantity === 0) {
            zeroQ += "<tr class='reorder'>" + fillRow(row) + "</tr>";
        } else if (row.quantity <= row.reorderThreshold) {
            oneQ += "<tr class='low'>" + fillRow(row) + "</tr>";
        } else {
            twoQ += "<tr>" + fillRow(row) + "</tr>";
        }
    }
    inner += zeroQ + oneQ + twoQ;
    table += inner;
    table += "</tbody></table>";
    viewScreen.innerHTML = table;
}

function fillRow(row) {
    let fill = "";
    fill += "<td><div>" + row.itemID + "</div></td>";
    fill += "<td><div>" + row.itemName + "</div></td>";
    fill += "<td><div>" + row.categoryName + "</div></td>";
    fill += "<td><div>" + row.sku + "</div></td>";
    fill += "<td><div>" + row.companyName + "</div></td>";
    fill += "<td><div>" + row.quantity + "</div></td>";
    return fill;
}

function searchTable() {
    let table, tr, td, i;
    let itemID = document.querySelector("#searchItemID").value;
    let itemName = document.querySelector("#searchItemName").value.toUpperCase();
    let category = document.querySelector("#searchCategory").value.toUpperCase();
    let sku = document.querySelector("#searchSku").value;

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
        let skew = td[3].textContent || td[3].innerText;

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

        if (sku !== "") {
            if (skew.indexOf(sku) > -1) {
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



