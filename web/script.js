var baseurl = "http://localhost:8981/elsets";

function loadElsets() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET",baseurl,true);
    xmlhttp.onreadystatechange = function() {
      if (xmlhttp.readyState ===4 && xmlhttp.status ===200) {
        console.log(xmlhttp.responseText);
        var elsets = JSON.parse(xmlhttp.responseText);
        var tbltop = `<table>
            <tr border="2"><th>Name</th><th>Satellite Number</th></tr>`;
        var main ="";
        for (i = 0; i < elsets.length; i++){
            main += "<tr><td>"+
                elsets[i].name+"</td><td>"+
                elsets[i].satelliteNumber+"</td></tr>";
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