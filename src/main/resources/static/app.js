document.addEventListener("DOMContentLoaded", function() {

    let token = document.getElementById("token").value;

    document.getElementById("addValue").addEventListener("click", function() {
        let valueToAdd = document.getElementById("valueToAdd").value;
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.status == 200 && this.readyState == 4) {
                location.reload();
            }
        };
        xhr.open("POST", "http://localhost:8080/stats/api/" + token);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify({
            "value": valueToAdd
        }));
    });

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.status == 200 && this.readyState == 4) {
            let stat = JSON.parse(this.responseText);
            let labels = [];
            let data = [];
            for (const statValue of stat.statValues) {
                labels.push(statValue.createDate);
                data.push(statValue.value);
            }
            let context = document.getElementById("chart").getContext("2d");
            new Chart(context, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        borderColor: 'rgb(255,14,17)',
                        data: data
                    }]
                },
                options: {
                    legend: {
                        display: false
                    }
                }
            });
        }
    };
    xhr.open("GET", "http://localhost:8080/stats/api/" + token);
    xhr.send();

});