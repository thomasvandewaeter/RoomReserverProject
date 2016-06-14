jQuery.ajax({
    url: "https://api.apixu.com/v1/current.json?key=36c5f01e00b14e578c0144635161306&q=Leuven",
    type: "GET",
    contentType: 'application/json; charset=utf-8',
    success: function (jsonData, textStatus, jqXHR) {
        document.getElementById("weatherdiscription").innerHTML = jsonData.current.condition.text;
        var img = document.getElementById("weatherImg");

        img.src = jsonData.current.condition.icon;
    },
    error: function (jqXHR, textStatus, errorThrown) {
    },
    timeout: 120000
});