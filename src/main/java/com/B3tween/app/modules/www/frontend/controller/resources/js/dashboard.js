document.addEventListener('DOMContentLoaded', async function () {
    try {
        // Get token container
        const tokenContainer = document.getElementById("proxyToken");
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
            tokenContainer.innerText = data.proxyToken
        })
    } catch (error) {
        console.error("Error" + error);
    }
});