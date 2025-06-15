document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("loginForm").addEventListener('submit', async function (form) {
        form.preventDefault();
        // Get result string
        const resultString = document.getElementById("statusResponse");
        // Get form data
        const formData = new FormData(this);
        // Convert form to JSON
        const JSONdata = Object.fromEntries(formData.entries());
        const JSONString = JSON.stringify(JSONdata);
        // Send fetch
        try {
            await fetch(apiHost+"/auth/login", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Content-Length': JSONString.length
                },
                body: JSONString 
            }).then((response) => {
                return response.json();
            }).then((data) => {
                switch (data.status) {
                    case 409:
                        resultString.style.display = "inline";
                        resultString.innerText = loginError;
                        break;
                    case 302:
                        document.cookie = data.cookie;
                        localStorage.setItem("uid", data.uid);
                        window.location = data.location;
                        break;
                }
            });
        } catch (error) {
            console.error("Error: " + error);
        }
    });
});
