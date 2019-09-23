document.addEventListener("DOMContentLoaded", function() {

    let token = document.querySelector("#token").value;

    document.querySelector("#addValue").addEventListener("click", function() {
        let valueToAdd = document.querySelector("#valueToAdd").value;
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.status == 200 && this.readyState == 4) {
                location.href = "http://localhost:8080/stats/" + token;
            }
        };
        xhr.open("POST", "http://localhost:8080/stats/api/" + token);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify({
            "value": valueToAdd
        }));
    });

    // render chart
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (this.status == 200 && this.readyState == 4) {
            let stat = JSON.parse(this.responseText);
            if (stat.numeric) {
                let chartCanvas = document.createElement("canvas");
                chartCanvas.setAttribute("id", "chartCanvas");
                document.querySelector("#chart").appendChild(chartCanvas);

                let labels = [], data = [];
                for (const statValue of stat.statValues) {
                    labels.push(moment(statValue.createDate).format('L LTS'));
                    data.push(statValue.value);
                }

                let context = chartCanvas.getContext("2d");
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
        }
    };
    xhr.open("GET", "http://localhost:8080/stats/api/" + token);
    xhr.send();

});