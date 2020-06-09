
addOrUpdate = "add";
//onload code
//<editor-fold>
window.onload = function () {

    userRole = document.querySelector("#perms").value.replace(/(\r\n|\n|\r)/gm, "");
    document.querySelector("#panel").addEventListener("click", handleClick);
    userID = document.querySelector("#id").value.replace(/(\r\n|\n|\r)/gm, "");
    userLoc = document.querySelector("#loc").value.replace(/(\r\n|\n|\r)/gm, "");
    //if a user doesn't have a role, they automatically get logged out.
    if (userRole !== "") {
        document.querySelector("#itmTable").addEventListener("click", rowClicker);
        document.querySelector("#invTable").addEventListener("click", rowClicker);
        document.querySelector("#locTable").addEventListener("click", rowClicker);
        document.querySelector("#usrTable").addEventListener("click", rowClicker);
        document.querySelector("#couTable").addEventListener("click", rowClicker);
        document.querySelector("#top").addEventListener("click", clickTab);
        document.querySelector("#sidebnav").addEventListener("click", clickTab);
        document.querySelector("#invLocation").addEventListener("change", locationSwap);
        document.querySelector("#logOut").addEventListener("click", logOut);
        document.querySelector('.orderList').addEventListener("change", getDetails);
        document.querySelector("#editOrder").addEventListener("click", checkOrders);
        document.querySelector("#createOrder").addEventListener("click", createOrder);
        document.querySelector("#startEmerg").addEventListener("click", createOrder);
        document.querySelector("#nextStatus").addEventListener("click", forwardStatus);
        document.querySelector("#returnStore").addEventListener("click", forwardStatus);
        document.querySelector("#ordLocation").addEventListener("change", filterOrders);
        document.querySelector("#openSide").addEventListener("click", openNav);
        document.querySelector("#loss").addEventListener("click", goLoss);
        document.querySelector(".closebtn").addEventListener("click", closeNav);
    } else {
        window.location.replace("index.jsp");
    }
    initPermissions = {};
    loadPerms();
    fillPerms(userRole);
    loadCombos();
};

//</editor-fold>
//fill combo boxes
// <editor-fold>

function goLoss() {
    window.location.replace("loss.jsp");
}
function loadCombos() {
    //get provinces
    pullComboData("province", "ProvinceServlet/Provinces/");
    //get role id
    pullComboData("role", "RoleServlet/Roles/");
    //get location type
    pullComboData("lType", "LTypeServlet/LType/");
    //get location ID
    pullComboData("locID", "LocServlet/LocID/");
    //get suppliers
    pullComboData("supplier", "SuServlet/SupplierID/");
    //get categories
    pullComboData("category", "CatServlet/Categories/");
}

function pullComboData(type, url) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                fillCombos(response, type);
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function fillCombos(text, type) {
    let arr = JSON.parse(text);
    let id = "." + type;
    let select = document.querySelectorAll(id);
    for (let t = 0; t < select.length; t++) {
        let fill = "";
        if (type === "province") {
            for (let i = 0; i < arr.length; i++) {

                fill += "<option value='" + arr[i].provinceID + "'";
                if (arr[i].provinceID === "NB") {
                    fill += "selected";
                }
                fill += ">" + arr[i].longName + "</option>";
            }
        } else if (type === "locID") {
            for (let i = 0; i < arr.length; i++) {
                fill += "<option value='" + arr[i].locationID + "'";
                if (arr[i].locationID === userLoc) {
                    fill += "selected";
                }
                fill += ">" + arr[i].locationID + "</option>";
            }
        } else if (type === "role") {
            for (let i = 0; i < arr.length; i++) {
                fill += "<option value='" + arr[i].roleID + "'>" + arr[i].roleID + "</option>";
            }
        } else if (type === "lType") {
            for (let i = 0; i < arr.length; i++) {
                fill += "<option value='" + arr[i].locationTypeID + "'>" + arr[i].locationTypeID + "</option>";
            }
        } else if (type === "supplier") {
            for (let i = 0; i < arr.length; i++) {
                fill += "<option value='" + arr[i].supplierID + "'>" + arr[i].companyName + "</option>";
            }
        } else if (type === "category") {
            for (let i = 0; i < arr.length; i++) {
                fill += "<option value='" + arr[i].categoryName + "'>" + arr[i].categoryName + "</option>";
            }
        }
        select[t].innerHTML = fill;
    }
}
// </editor-fold>
//set user permissions
//<editor-fold>
function logOut() {
    sessionStorage.clear();
    window.location.replace("index.jsp");
}

function loadPerms() {
    url = "PermsServlet/Perms/";
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                setPerms(response, "init");
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function fillPerms(user) {
    url = "PermsServlet/Perms/ID/" + user;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                setPerms(response, user);
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function setPerms(resp, user) {
    let arr = JSON.parse(resp);
    if (user === "init") {
        for (let i = 0; i < arr.length; i++) {
            initPermissions[arr[i].actionID] = false;
        }
    } else {
        let temp = {};
        for (let i = 0; i < arr.length; i++) {
            temp[arr[i].actionID] = false;
        }

        let keys = Object.keys(initPermissions);
        let keys2 = Object.keys(temp);
        for (let i = 0; i < keys2.length; i++) {
            if (initPermissions.hasOwnProperty(keys2[i])) {
                let perm = keys2[i];
                initPermissions[perm] = true;
            }
        }
        setButtons();
    }
    setTabs(user);
}

function setTabs(perms) {
    role = perms.replace(/(\r\n|\n|\r)/gm, "");
    let inv = document.querySelector("#inventory");
    let ord = document.querySelector("#order");
    let rep = document.querySelector("#report");
    let use = document.querySelector("#users");
    let loc = document.querySelector("#locations");
    let rStore = document.querySelector("#returnStore");
    let buttons = document.querySelector(".buttons");
    let loss = document.querySelector("#loss");
    switch (role) {
        case "Driver":
            use.parentNode.removeChild(use);
            loc.parentNode.removeChild(loc);
            buttons.parentNode.removeChild(buttons);
            loss.parentNode.removeChild(loss);
            ord.click();
            break;
        case "Finance":
            rep.click();
            buttons.parentNode.removeChild(buttons);
            loss.parentNode.removeChild(loss);
            break;
        case "Manager":
            rep.click();
            break;
        case "Store Manager":
            inv.click();
            rStore.parentNode.removeChild(rStore);
            break;
        case "Store Worker":
            use.parentNode.removeChild(use);
            buttons.parentNode.removeChild(buttons);
            loss.parentNode.removeChild(loss);
            inv.click();
            break;
        case "Warehouse Manager":
            inv.click();
            break;
        case "Warehouse Worker":
            use.parentNode.removeChild(use);
            buttons.parentNode.removeChild(buttons);
            loss.parentNode.removeChild(loss);
            inv.click();
            break;
        default:
            use.click();
            break;
    }
}

function setButtons() {
    let add, update, submit;
    add = document.querySelectorAll("#addBtn");
    update = document.querySelectorAll("#upd8Btn");
    submit = document.querySelectorAll("#submitCrud");
    let inputs = document.getElementsByTagName("input");
    if (!initPermissions["CRUD"] === true) {
        for (let j = 0; j < inputs.length; j++) {
            if (inputs[j].type === "checkbox") {
                inputs[j].parentNode.removeChild(inputs[j]);
            }
        }

        add[1].parentNode.removeChild(add[1]);
        if (!initPermissions["CREATE ITEM"]) {
            add[0].parentNode.removeChild(add[0]);
        }
        if (!initPermissions["CREATE COURIER"]) {
            add[2].parentNode.removeChild(add[2]);
        }
        if (initPermissions["READ USER"]) {
            add[3].parentNode.removeChild(add[3]);
            update[3].parentNode.removeChild(update[3]);
        }

        if (!initPermissions["UPDATE COURIER"] && !initPermissions["UPDATE ITEM"]) {
            for (let j = 0; j < update.length; j++) {
                update[j].parentNode.removeChild(update[j]);
            }
        }
    }
}

//</editor-fold>
//building tabs and tables
//<editor-fold>
function clickTab(e) {
    let tab = e.target.id.toLowerCase();
    if (tab !== "side") {

        let tabConcat = tab + "View";
        let screens = document.querySelectorAll(".screen");
        for (let i = 0; i < screens.length; i++) {
            screens[i].classList.add("hidden");
        }
        document.querySelector("#" + tabConcat).classList.remove("hidden");
        loadTab(tab);

    }
}

function loadTab(tab) {
//basically just a routing function. loads in data.
    switch (tab) {
        case "inventory":
            setActiveTab(tab);
            loadData("inv", getUrl("inv"));
            break;
        case "order":
            setActiveTab(tab);
            orderScreen();
            break;
        case "report":
            setActiveTab(tab);
            break;
        case "items":
            setActiveTab(tab);
            loadData("itm", getUrl("itm"));
            break;
        case "locations":
            setActiveTab(tab);
            loadData("loc", getUrl("loc"));
            break;
        case "couriers":
            setActiveTab(tab);
            loadData("cou", getUrl("cou"));
            break;
        case "users":
            setActiveTab(tab);
            loadData("usr", getUrl("usr"));
            break;
        default:
            break;
    }
}

function setActiveTab(tab) {
    let tabs = document.querySelector("#top").childNodes;
    for (let i = 0; i < tabs.length; i++) {
        if (tabs[i].classList.contains("active")) {
            tabs[i].classList.remove("active");
        }
    }
    let active = document.getElementById(tab);
    active.classList.add("active");
}

function locationSwap() {
    let swapper = document.getElementById("invLocation");
    let location = swapper.value;
    loadData("inv", "InvServlet/invItems/" + location);
}

function loadData(tName, url) {
    clearAll();
    if (tName === "inv") {
        let swapper = document.getElementById("invLocation");
        let location = swapper.value;
        url = "InvServlet/invItems/" + location;
    }
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Inventory not found.");
                console.log(response);
            } else {
                buildTable(response, tName);
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function buildTable(resp, tName) {
    let id = "#" + tName + "Table";
    let viewScreen = document.querySelector(id);
    let headers = grabHeaders(tName);
    let table = "<table class=" + tName + "><thead><tr>";
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
    let keys = Object.keys(row);
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i];
        if (key === "active") {
            break;
        } else {
            fill += "<td><div>" + row[keys[i]] + "</div></td>";
        }
    }
    return fill;
}
//</editor-fold>
//click handler for the entire main panel. it's ugly, but it works.
//<editor-fold>
function handleClick(e) {
    let targ = e.target;
    let type = targ.parentNode.className;
    let tab = e.target.id.toLowerCase();
    if (tab === "portal") {
        window.location.replace("webPortal.jsp");
    }
    if (targ.id === "searchBtn") {
        let search = type + "Search";
        let table = type + "Table";
        searchTable(search, table);
    } else if (targ.id === "submitCrud") {
        let type = targ.parentNode.classList;
        handleSubmit(type);
    } else if (targ.id === "cancel") {
        clearAll();
    } else if (targ.id === "addBtn") {
        addOrUpdate = "add";
        addOpen(targ.parentNode.className);
    } else if (targ.id === "upd8Btn") {
        addOrUpdate = "update";
        addOpen(targ.parentNode.className);
    } else if (targ.tagName === "TH" && targ.offsetParent.parentNode.id !== "ordTable") {
        let loc;
        let swapper = document.getElementById("invLocation");
        let location = swapper.value;
        if (userLoc === "CORP") {
            loc = "STJN";
        } else {
            loc = location;
        }
        let parent = targ.offsetParent.className;
        let header = targ.innerHTML.split(' ').join('_');
        url = "";
        if (parent === "inv") {
            url = "InvServlet/invItems/sort+" + loc + "+" + header;
        } else {
            url = getUrl(parent) + "sort+" + header;
        }

        loadData(parent, url);
    } else if (targ.tagName === "TD" && targ.offsetParent.parentNode.id === "ordTable") {
        rowClicker(e);
    }
}
//</editor-fold>
//frontend search because it's faster and doesn't cause a memory leak!
//<editor-fold>
function searchTable(input, id) {
    var filter, table, tr, td, th, i, txtValue;
    input = document.getElementById(input);
    filter = input.value.toUpperCase().split(" ");
    table = document.getElementById(id);
    tr = table.getElementsByTagName("tr");
    th = table.getElementsByTagName("th");
    for (let i = 0; i < filter.length; i++) {
        for (let j = 0; j < tr.length; j++) {
            for (let k = 0; k < th.length; k++) {
                td = tr[j].getElementsByTagName("td")[k];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter[i]) > -1) {
                        tr[j].style.display = "";
                        break;
                    } else {
                        tr[j].style.display = "none";
                    }
                }
            }
        }
    }
}
//</editor-fold>
//crud
// <editor-fold>

function handleSubmit(type) {
    let url, id, obj, tName;
    let err = false;
    let bool = false;
    if (!type.contains("hidden")) {
        if (type.contains("courier")) {
            let courier = document.querySelectorAll(".inCou");
            let inputs = [];
            for (let i = 0; i < courier.length; i++) {
                inputs.push(courier[i].value);
            }
            let lastID;
            if (addOrUpdate === "add") {
                let getLastID = document.querySelectorAll(".cou tr");
                lastID = getLastID[getLastID.length - 1].cells[0].innerText;
            } else {
                lastID = document.querySelector("#couHidden");
            }

            url = "CourierServlet/Couriers/";
            id = lastID;
            tName = "cou";
            if (courier[9].checked === true) {
                bool = true;
            }

            if (checkPostal(inputs[5], inputs[6])) {
                if (checkEmail(inputs[7])) {
                    obj = {
                        "courierID": inputs[0],
                        "courierName": inputs[1],
                        "address": inputs[2],
                        "city": inputs[3],
                        "provinceID": inputs[4],
                        "postalCode": inputs[5],
                        "country": inputs[6],
                        'courierEmail': inputs[7],
                        "courierPhone": inputs[8],
                        'notes': inputs[9],
                        "active": bool
                    };
                } else {
                    err = true;
                    alert("Not a valid email address.");
                }
            } else {
                err = true;
                alert("Postal/Zip code is not formatted correctly.");
            }


            console.log(obj);
        } else if (type.contains("locations")) {
            let locations = document.querySelectorAll(".inLoc");
            let inputs = [];
            for (let i = 0; i < locations.length; i++) {
                inputs.push(locations[i].value);
            }

            let bool = false;
            url = "LocServlet/Locations/";
            id = inputs[0];
            tName = "loc";
            if (locations[9].checked === true) {
                bool = true;
            }

            if (checkPostal(inputs[5], inputs[6])) {
                obj = {
                    "locationID": inputs[0],
                    "description": inputs[1],
                    "address": inputs[2],
                    "city": inputs[3],
                    "province": inputs[4],
                    "postalCode": inputs[5],
                    "country": inputs[6],
                    'locationTypeID': inputs[7],
                    "deliveryDay": inputs[8],
                    "active": bool
                };
            } else {
                err = true;
                alert("Postal/Zip code is not formatted correctly.");
            }
        }
//user
        else if (type.contains("user")) {
            let users = document.querySelectorAll(".inUsr");
            let inputs = [];
            for (let i = 0; i < users.length; i++) {
                inputs.push(users[i].value);
            }
            let bool = false;
            if (users[4].checked === true) {
                bool = true;
            }
            url = "UserServlet/Users/";
            id = inputs[0];
            tName = "usr";
            if (addOrUpdate === "add" && inputs[3] === "") {
                alert("When creating a user, please give them a password.");
                err = true;
            } else {
                if (inputs[3] === "") {
                    obj = {
                        "userID": inputs[0],
                        "locationID": inputs[1],
                        "roleID": inputs[2],
                        "active": bool
                    };
                } else {
                    if (checkPassword(inputs[3])) {
                        obj = {
                            "userID": inputs[0],
                            "locationID": inputs[1],
                            "roleID": inputs[2],
                            "password": inputs[3],
                            "active": bool
                        };
                    } else {
                        alert('Password must be a minimum of 8 characters, one capital, one numeric.');
                        err = true;
                    }

                }
            }
        }
//items
        else if (type.contains("items")) {
            let items = document.querySelectorAll(".inItm");
            let inputs = [];
            for (let i = 0; i < items.length; i++) {
                inputs.push(items[i].value);
            }


            if (items[11].checked === true) {
                bool = true;
            }
            url = "ItemServlet/Items/";
            id = inputs[0];
            tName = "itm";
            obj = {
                "itemID": inputs[0],
                "itemName": inputs[1],
                "sku": inputs[2],
                "description": inputs[3],
                "categoryName": inputs[4],
                "weight": inputs[5],
                "costPrice": inputs[6],
                'retailPrice': inputs[7],
                "supplierID": inputs[8],
                "caseSize": inputs[9],
                "notes": inputs[10],
                "active": inputs[11]
            };
        } else if (type.contains("inventory")) {
            let quantities = document.querySelectorAll(".inInv");
            let inputs = [];
            for (let i = 0; i < quantities.length; i++) {
                inputs.push(quantities[i].value);
            }

            url = "InvServlet/invItems/";
            tName = "inv";
            id = inputs[0];
            obj = {
                "itemID": inputs[0],
                "locationID": inputs[1],
                "reorderThreshold": inputs[2],
                "reorderLevel": inputs[3],
                "quantity": inputs[4]
            };
        }
    }

    if (err === false) {
        doUpdate(url, id, obj, tName);
    }

}

function checkEmail(input) {
    var email = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
    if (email.test(input))
    {
        return true;
    } else
    {
        return false;
    }
}

function checkPostal(input, country) {
    if (country === "United States") {
        var postal = /^([0-9]{5}(?:-[0-9]{4})?$/;
        if (postal.test(input))
        {
            return true;
        } else
        {
            return false;
        }
    } else if (country === "Canada") {
        var postal = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/;
        if (postal.test(input))
        {
            return true;
        } else
        {
            return false;
        }
    }
}

function checkPassword(inputtxt)
{
    var passw = /^(?=.*\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,})$/;
    if (passw.test(inputtxt))
    {
        return true;
    } else
    {
        return false;
    }
}

function doUpdate(link, id, obj, tName) {
    let url = link + id;
    let method = (addOrUpdate === "add") ? "POST" : "PUT";
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            var resp = xmlhttp.responseText;
            if (resp === "false") {
                alert("¯\_(ツ)_/¯");
                console.log(resp);
            } else {
                loadData(tName, url);
            }
        }
    };
    xmlhttp.open(method, url, true);
    xmlhttp.send(JSON.stringify(obj));
}

function rowClicker(e) {
    clearTable();
    clearAll();
    if (e !== undefined) {
        if (e.target.parentElement.tagName === "TR") {
            e.target.parentElement.classList.add("clicked");
        } else if (e.target.parentElement.parentElement.tagName === "TR") {
            e.target.parentElement.parentElement.classList.add("clicked");
        }
        let updates = document.querySelectorAll("#upd8Btn");
        for (let i = 0; i < updates.length; i++) {
            updates[i].removeAttribute("disabled");
        }
    }
}

function clearTable() {
    let rows, i;
    rows = document.querySelectorAll("tr");
    for (i = 0; i < rows.length; i++) {
        rows[i].classList.remove("clicked");
    }
}

function clearAll() {
    let elements = document.getElementsByTagName("input");
    for (let i = 0; i < elements.length; i++) {
        if (elements[i].type === "text") {
            elements[i].value = "";
        }
    }
    let panels = document.querySelectorAll(".update");
    for (let i = 0; i < panels.length; i++) {
        if (panels[i].classList.contains("hidden")) {
        } else {
            panels[i].classList.add("hidden");
        }
    }

    let selects = document.querySelectorAll(".inUsr");
    for (let i = 0; i < selects.length; i++) {
        if (selects[i].disabled === false) {
            selects[i].disabled = true;
        }
    }

    let adds = document.querySelectorAll("#addBtn");
    for (let i = 0; i < adds.length; i++) {
        if (adds[i].disabled === true) {
            adds[i].disabled = false;
        }
    }

    let locations = document.getElementsByTagName("select");
    for (let i = 0; i < locations.length; i++) {

    }

    clearTable();
}
// </editor-fold>
// main crud turn on
// <editor-fold>
function addOpen(table) {
    let id = "#" + table + "Update";
    let cap = table.charAt(0).toUpperCase() + table.slice(1);
    let inputs = "in" + cap;
    let elements = document.getElementsByTagName("input");
    let updater = document.querySelector(id);
    let betterElements = document.querySelectorAll("." + inputs);
    if (updater.classList.contains("hidden")) {
        if (table !== "usr") {
            updater.classList.remove("hidden");
        }

    } else {
        if (table !== "usr") {
            updater.classList.add("hidden");
            for (let i = 0; i < elements.length; i++) {
                if (elements[i].type === "text") {
                    elements[i].value = "";
                }
            }
        }
    }

    if (table === "usr") {
        let selects = document.querySelectorAll(".inUsr");
        for (let i = 0; i < selects.length; i++) {
            if (selects[i].disabled === true) {
                selects[i].disabled = false;
            }
        }
    }

    if (addOrUpdate === "add" && table === "cou") {
        let lastID;
        let getLastID = document.querySelectorAll(".cou tr");
        lastID = getLastID.length;
        betterElements[0].value = lastID;
    }

    if (addOrUpdate === "update") {
        let row = document.querySelector(".clicked");
        let list = row.cells;
        if (table !== "inv") {
            for (let j = 0; j < list.length; j++) {
                betterElements[j].value = list[j].firstChild.innerHTML;
            }
        } else {
            betterElements[0].value = list[0].firstChild.innerHTML;
            betterElements[1].value = list[1].firstChild.innerHTML;
            betterElements[2].value = list[10].firstChild.innerHTML;
            betterElements[3].value = list[11].firstChild.innerHTML;
            betterElements[4].value = list[9].firstChild.innerHTML;
        }

        let updates = document.querySelectorAll("#upd8Btn");
        for (let i = 0; i < updates.length; i++) {
            if (updates[i].disabled === false) {
                updates[i].disabled = true;
            }
        }

        let adds = document.querySelectorAll("#addBtn");
        for (let i = 0; i < adds.length; i++) {
            if (adds[i].disabled === false) {
                adds[i].disabled = true;
            }
        }
    }
}
//</editor-fold>
//this is a function to store and retrieve headers and grab urls dynamically until I figure out a better way to do it.
// <editor-fold>
function grabHeaders(tableName) {
    let table;
    if (tableName === "inv") {
        table = ["ID", "Location", "Item Name", "Category", "SKU", "Description", "Supplier", "Wgt.", "Case Size", "In Stock", "Threshold", "Level"];
    } else if (tableName === "cou") {
        table = ["ID", "Company", "Address", "City", "Province", "Postal Code", "Country", "Email", "Phone", "Notes"];
    } else if (tableName === "loc") {
        table = ["ID", "Description", "Address", "City", "Province", "Postal Code", "Country", "Type", "Delivery Day"];
    } else if (tableName === "usr") {
        table = ["ID", "Location", "Role"];
    } else if (tableName === "itm") {
        table = ["ID", "Name", "SKU", "Description", "Category", "Wgt.", "Cost", "Retail", "Supplier", "Case Size", "Notes"];
    } else if (tableName === "ord") {
        table = ["ID", "Name", "Category", "SKU", "Weight", "Case Size", "Ordering"];
    }

    return table;
}

function getUrl(tName) {
    let url = "";
    let loc;
    if (userLoc === "CORP") {
        loc = "STJN";
    } else {
        loc = userLoc;
    }

    if (tName === "inv") {
        url = "InvServlet/invItems/" + loc;
    } else if (tName === "cou") {
        url = "CourierServlet/Couriers/";
    } else if (tName === "loc") {
        url = "LocServlet/Locations/";
    } else if (tName === "usr") {
        url = "UserServlet/Users/";
    } else if (tName === "itm") {
        url = "ItemServlet/Items/";
    }

    return url;
}
//</editor-fold>

function orderScreen() {

    let filter = document.querySelector('#ordLocation');
    let url = "";
    filter.innerHTML += "<option value = ''> </option>";
    let role = userRole;
    if (role === "Warehouse Manager") {
        url = "OrdServlet/Orders/out/";
    } else {
        url = "OrdServlet/Orders/";
    }


    if (role !== "DB Admin" && role !== "Warehouse Manager") {
        for (var i = 0, j = filter.options.length; i < j; ++i) {
            if (filter.options[i].innerHTML === userLoc) {
                filter.selectedIndex = i;
                url += filter.options[i].value;
                break;
            }
        }
    } else {
        filter.selectedIndex = filter.length;
    }

    getOrders(url, "list"); //get all applicable orders. this is just to fill the select box.
}

function getOrders(url, type) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                if (type === "list") {
                    fillOrd(response);
                } else if (type === "address") {
                    fillAddress(response);
                } else if (type === "items") {
                    fillItems(response);
                }
            }
        }
    };
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}

function fillOrd(text) {

    let arr = JSON.parse(text);
    let buildString = "";
    let orderList = document.querySelector('.orderList');
    orderList.innerHTML = "";
    let noteString = "";
    for (let i = 0; i < arr.length; i++) {
        buildString += "<option value = '" + arr[i].transactionID + "' class='" + arr[i].transactionType + "'>";
        buildString += arr[i].transactionID + " | ";
        buildString += arr[i].originalLocationID + " | ";
        buildString += arr[i].creationDate + " | ";
        buildString += arr[i].transactionStatus + " | ";
        buildString += arr[i].transactionType;
        buildString += "</option>";
        noteString += "<input type='hidden' value='" + arr[i].notes + "' class='note " + arr[i].transactionID + "'>";
    }
    orderList.innerHTML = buildString;
    document.querySelector('#bottomHalf').innerHTML += noteString;
    orderList.selectedIndex = 0;
    orderList.dispatchEvent(new Event('change'));

    if (arr.length === 0) {
        let box = document.querySelector(".shippingInfo");
        box.innerHTML += "No current oustanding orders.";
    }
}

function getDetails(e) {
    let target = e.target;
    let value = target.options[target.selectedIndex].innerText;
    let valArray = value.split(" | ");
    let transactionID = valArray[0];
    let locID = valArray[1];
    console.log(value);
    let notes = document.querySelector(".notes");
    let noteFill = "";
    let bHalf = document.querySelectorAll("#bottomHalf input");
    for (let i = 0; i < bHalf.length; i++) {
        if (bHalf[i].classList.contains(transactionID)) {
            noteFill = bHalf[i].value;
        }
    }
    if (noteFill === "undefined" || noteFill === undefined || noteFill === null) {
        noteFill = "";
    }
    notes.innerHTML = "<p><b>Notes:</b></p>" + noteFill;
    getOrders("LocServlet/Locations/" + locID, "address");
    getOrders("InvServlet/tLine/" + transactionID, "items");
    
}

function fillAddress(text) {
    let arr = JSON.parse(text);
    console.log(arr);
    let box = document.querySelector(".shippingInfo");
    box.innerHTML = "";
    let addressString = "";
    addressString += "<p><b>Location: </b>" + arr.description + "</p>";
    addressString += "<p><b>Address: </b>" + arr.address + "</p>";
    addressString += "<p><b>City: </b>" + arr.city + "</p>";
    addressString += "<p><b>Province: </b>" + arr.province + "</p>";
    addressString += "<p><b>Postal Code: </b>" + arr.postalCode + "</p>";
    addressString += "<p><b>Delivery Day: </b>" + arr.deliveryDay + "</p>";
    box.innerHTML += addressString;
}

function fillItems(text) {
    let arr = JSON.parse(text);
    console.log(arr);
    let ordTable = document.querySelector('#ordTable');
    let fillTable = "";
    let headers = grabHeaders("ord");
    fillTable += "<table><thead>";
    fillTable += "<tr>";
    for (let i = 0; i < headers.length; i++) {
        fillTable += "<th>" + headers[i];
        +"</th>";
    }
    fillTable += "</tr></thead>";
    fillTable += "<tbody>";
    let loopFill = "";
    for (let j = 0; j < arr.length; j++) {
        loopFill += "<tr>";
        loopFill += "<td>" + arr[j].itemID + "</td>";
        loopFill += "<td>" + arr[j].itemName + "</td>";
        loopFill += "<td>" + arr[j].categoryName + "</td>";
        loopFill += "<td>" + arr[j].sku + "</td>";
        loopFill += "<td>" + arr[j].weight + "</td>";
        loopFill += "<td>" + arr[j].caseSize + "</td>";
        loopFill += "<td>" + "<input type='number' value = '" + arr[j].quantity + "' disabled></td>";
        loopFill += "</tr>";
    }
    fillTable += loopFill + "</tbody></table>";
    ordTable.innerHTML = fillTable;
}

function checkOrders(e) {
    let order = document.querySelector(".orderList").selectedOptions[0].label;
    let item = document.querySelector(".clicked");
    let split = order.split("|");
    if (order === "") {
        alert("Please select an order.");
    } else {
        if (split[1].replace(/\s/g, '') !== userLoc && userID !== "admin" && userLoc !== "WARE") {
            alert("You cannot edit an order from another location.");
        } else {
            if (e.target.id === "editOrder") {
                try {
                    if (item.cells[6].childNodes[0].disabled === false) {
                        submitItem(split[0], item.cells[0].childNodes[0], item.cells[6].childNodes[0].value);
                    } else if (split[3].replace(/\s/g, '') !== "NEW") {
                        alert("You cannot edit an order that isn't new.");
                    } else {
                        item.cells[6].childNodes[0].disabled = false;
                    }
                } catch (Exception) {
                    alert("Item not found!");
                }
            } else {

            }
        }
    }
    console.log(order, item);
}

function submitItem(orderID, itemID, quantity) {
    let url = "InvServlet/update/" + orderID + "/" + itemID + "/" + quantity;

    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                orderScreen();
            }
        }
    };
    xmlhttp.open("POST", url, true);
    xmlhttp.send();
}

function filterOrders() {
    let location = document.querySelector("#ordLocation").value;
    if (location !== "" && location !== "CORP") {
        getOrders("OrdServlet/Orders/Filter/" + location, "list");
    } else {
        orderScreen();
    }
}

function forwardStatus(e) {
    let button = e.target.id;
    let order = document.querySelector(".orderList").selectedOptions[0].label;
    let split = order.split(" | ");
    let id = split[0];
    let location = split[1];
    let status = split[3];
    let notes = "";
    let upStatus = "";

    if (button === "nextStatus") {
        if (split[1].replace(/\s/g, '') !== userLoc && userID !== "Admin" && userLoc !== "WARE") {
            alert("You cannot edit an order from another location.");
        } else {
            switch (status) {
                case "NEW":
                    upStatus = "SUBMITTED";
                    break;
                case "SUBMITTED":
                    upStatus = "PROCESSING";
                    checkWarehouse(id);
                    break;
                case "PROCESSING":
                    upStatus = "READY";
                    break;
                case "READY":
                    upStatus = "IN TRANSIT";
                    break;
                case "IN TRANSIT":
                    upStatus = "DELIVERED";
                    break;
                case "DELIVERED":
                    upStatus = "COMPLETE";
                    break;
                default:
                    break;
            }
            moveStatus(upStatus, location, id, notes);
        }
    } else if (button === "returnStore") {
        if (status !== "SUBMITTED") {
            alert("You cannot return an order of this status");
        } else if (userRole !== "DB Admin" && userRole !== "Warehouse Manager") {
            alert("You are not authorized to return an order to the store.");
        } else {
            moveStatus("NEW", location, id, notes);
        }
    }
}

function checkWarehouse(orderID) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                orderScreen();
            }
        }
    };
    xmlhttp.open("PUT", "OrdServlet/Orders/Update/CHECKWH/" + orderID, true);
    xmlhttp.send();
}

function moveStatus(upStatus, location, id, notes) {
    notes = "<br>Updated to status " + upStatus + " by " + userID;
    obj = {
        "transactionID": id,
        "originalLocationID": location,
        "transactionStatus": upStatus,
        "notes": notes
    };

    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                orderScreen();
            }
        }
    };
    xmlhttp.open("PUT", "OrdServlet/Orders/" + id, true);
    xmlhttp.send(JSON.stringify(obj));
}

function createOrder(e) {
    let button = e.target.id;
    let callFtn = "";
    if (button === "startEmerg") {
        callFtn = "emerg";
    } else {
        callFtn = "reg";
    }

    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let response = xmlhttp.responseText;
            if (response.search("ERROR") >= 0) {
                alert("Error: Not found.");
                console.log(response);
            } else {
                orderScreen();
            }
        }
    };
    xmlhttp.open("PUT", "OrdServlet/Orders/Update/" + userLoc + "/" + callFtn, true);
    xmlhttp.send();
}

function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
}
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}