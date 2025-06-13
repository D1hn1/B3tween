document.addEventListener('DOMContentLoaded', async function () {
    try {
        // Set user id
        const uidField = document.getElementById("uidField");
        uidField.innerText = localStorage.getItem("uid");
        // Get token container
        const rxField = document.getElementById("rxField");
        const txField = document.getElementById("txField");
        const connField = document.getElementById("connField");
        const tokenContainer = document.getElementById("proxyToken");
        const usernameContainer = document.getElementById("username-holder");
        // Default values
        rxField.innerText = 0;
        txField.innerText = 0;
        connField.innerText = 0;
        // Build JSON
        const data = `{"uid":"${localStorage.getItem("uid")}"}`;
        // Send fetch
        await fetch(apiHost+"/get-token", {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            },
            body: data
        }).then(response => {
                return response.json();
        }).then(data => {
            tokenContainer.innerText = data.proxyToken;
            usernameContainer.innerText = data.username;
        })
        // Fetch rxtx
        setInterval(async () => {
            await fetch(apiHost+"/get-rxtx", {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            },
            body: data
            }).then(response => {
                return response.json();
            }).then(data => {
                rxField.innerText = data.rx;
                txField.innerText = data.tx;
            })
        }, 3000);
        // Fetch conns
        setInterval(async () => {
            await fetch(apiHost+"/get-conns", {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Content-Length': data.length
                },
                body: data
            }).then(response => {
                return response.json();
            }).then(data => {
                connField.innerText = data.connections;
            })
        }, 3000);
    } catch (error) {
        console.error("Error" + error);
    }
});