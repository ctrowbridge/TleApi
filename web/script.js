var baseurl = "http://localhost:8981/elsets";
var elsets;
var sortOrderSat = 0;
var sortOrderEcc = 0;

function compareEcc(a, b) {
    if (sortOrderEcc == 0) {
        if (a.eccentricity > b.eccentricity) return 1;
        if (b.eccentricity > a.eccentricity) return -1;

    } else {
        if (a.eccentricity > b.eccentricity) return -1;
        if (b.eccentricity > a.eccentricity) return 1;
    }
    //document.getElementById("demo").innerHTML = "Sort by eccentricity " + sortOrderEcc;
}

function onSortEcc() {
    console.log(">>>>> onSortEcc");
    //document.getElementById("demo").innerHTML = "Sort by eccentricty " + sortOrderEcc;
    elsets.sort(compareEcc);
    sortOrderEcc = !sortOrderEcc;
    loadTable();
}

function compareSatno(a, b) {
    if (sortOrderSat == 0) {
        if (a.satelliteNumber > b.satelliteNumber) return 1;
        if (b.satelliteNumber > a.satelliteNumber) return -1;

    } else {
        if (a.satelliteNumber > b.satelliteNumber) return -1;
        if (b.satelliteNumber > a.satelliteNumber) return 1;
    }
    //document.getElementById("demo").innerHTML = "Sort by satellite " + sortOrderSat;
}

function onSortSatno() {
    console.log(">>>>> onSortSatno");
    //document.getElementById("demo").innerHTML = "Sort by satellite " + sortOrderSat;
    elsets.sort(compareSatno);
    sortOrderSat = !sortOrderSat;
    loadTable();
}

function addSortButtons() {

    var columnHTML = document.getElementById("sat-num-column").innerHTML;
    columnHTML += '<button id="sort-sat-num" onclick="onSortSatno()"><i aria-label="Sort by Satno" class="material-icons tiny">sort</i></button>';
    document.getElementById("sat-num-column").innerHTML = columnHTML;

    columnHTML = document.getElementById("ecc-num-column").innerHTML;
    columnHTML += '<button id="sort-ecc" onclick="onSortEcc()"><i aria-label = "Sort by Eccentricity" class="material-icons tiny">sort</i></button>';
    document.getElementById("ecc-num-column").innerHTML = columnHTML;
}

function loadTable() {

    document.getElementById("elsets").innerHTML = "";
    var tbltop = `<table class="striped responsive-table">
                <thead>
                <tr border="2">
                <th>Class</th>
                <th>Name</th>
                <th id="sat-num-column">Satellite<br>Number</th>
                <th>International<br>Designator</th>
                <th>Epoch<br>[YY DDD.DDDDDD]</th>
                <th id="ecc-num-column">Eccentricity </th>
                <th>Inclination<br>[deg]</th>
                <th>Right Ascension<br>[deg]</th>
                <th>Argument of Perigee<br>[deg]</th>
                <th>Mean Anomaly<br>[deg]</th>
                <th>Mean Motion<br>[revs/day]</th>
                </tr>
                </thead>
                <tbody>`;
            //console.log(">>> tbltop = ");
            //console.log(tbltop);

    var main ="";
    for (i = 0; i < elsets.length; i++) {
        //console.log(elsets[i]);
        main += "<tr>"+
            "<td>" + elsets[i].classification + "</td>" +
            "<td>" + elsets[i].name + "</td>" +
            "<td>" + elsets[i].satelliteNumber + "</td>" +
            "<td>" + elsets[i].internationalDesignator + "</td>" +
            "<td>" + elsets[i].epochYear + " " + elsets[i].epochDay + "</td>" +
            "<td>" + elsets[i].eccentricity.toFixed(6) + "</td>" +
            "<td>" + elsets[i].inclination.toFixed(3) + "</td>" +
            "<td>" + elsets[i].rightAscension.toFixed(3) + "</td>" +
            "<td>" + elsets[i].argumentOfPerigee.toFixed(3) + "</td>" +
            "<td>" + elsets[i].meanAnomaly.toFixed(3) + "</td>" +
            "<td>" + elsets[i].meanMotion.toFixed(4) + "</td>" +
            "</tr>";

        //console.log(">>> main = ");
        //console.log(main);
    }
    var tblbottom = "</tbody></table>";
    var tbl = tbltop + main + tblbottom;
    document.getElementById("elsets").innerHTML = tbl;
    addSortButtons();
}

function loadElsets() {

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", baseurl, true);

    xmlhttp.onreadystatechange = function() {

      if (xmlhttp.readyState ===4 && xmlhttp.status ===200) {
        //console.log(">>> responseText = ");
        //console.log(xmlhttp.responseText);
        elsets = JSON.parse(xmlhttp.responseText);
        console.log(">>> elsets:");
        console.log(typeof elsets);
        //console.log(elsets);
        loadTable();

      }
    };
    xmlhttp.send();
}

window.onload = function() {
    loadElsets();
}