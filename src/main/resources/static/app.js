document.addEventListener("DOMContentLoaded", function() {

    document.getElementById("addValue").addEventListener("click", function() {
        let valueToAdd = document.getElementById("valueToAdd").value;
        let token = document.getElementById("token").value;
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.status == 200) {
                location.reload();
            }
        };
        xhr.open("POST", "http://localhost:8080/stats/" + token);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify({
            "value": valueToAdd
        }));
    });

});