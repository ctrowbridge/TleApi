var baseurl = "http://localhost:8981/elsets";

function loadElsets() {

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", baseurl, true);

    xmlhttp.onreadystatechange = function() {

      if (xmlhttp.readyState ===4 && xmlhttp.status ===200) {
        console.log(">>> responseText = ");
        console.log(xmlhttp.responseText);
        var elsets = JSON.parse(xmlhttp.responseText);
        var tbltop = `<table>
            <tr border="2">
            <th>Class</th>
            <th>Name</th>
            <th>Satellite<br>Number</th>
            <th>International<br>Designator</th>
            <th>Epoch<br>[YY DDD.DDDDDD]</th>
            <th>Eccentricity</th>
            <th>Inclination<br>[deg]</th>
            <th>Right Ascension<br>[deg]</th>
            <th>Argument of Perigee<br>[deg]</th>
            <th>Mean Anomaly<br>[deg]</th>
            <th>Mean Motion<br>[revs/day]</th>
            </tr>`;
        console.log(">>> tbltop = ");
        console.log(tbltop);

        var main ="";
        for (i = 0; i < elsets.length; i++) {
            console.log(elsets[i]);
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

            console.log(">>> main = ");
            console.log(main);
        }
        var tblbottom = "</table>";
        var tbl = tbltop + main + tblbottom;
        document.getElementById("elsets").innerHTML = tbl;
      }
    };
    xmlhttp.send();
}
window.onload = function() {
    loadElsets();
}