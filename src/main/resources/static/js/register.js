document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("registerForm").addEventListener('submit', async function (form) {
        form.preventDefault();
        // Get result string
        const resultString = document.getElementById("statusResponse");
        // Get form data
        const formData = new FormData(this);
        // Convert form to JSON
        const JSONdata = Object.fromEntries(formData.entries());
        const JSONString = JSON.stringify(JSONdata);
        // Check for valid password
        if (JSONdata.password != JSONdata.confirm_password) {
            resultString.style.display = "inline";
            resultString.innerText = registerErrorPasswordsDoNotMatch;
            console.log(registerErrorPasswordsDoNotMatch);
            return;
        }
        // Send fetch
        try {
            await fetch(apiHost+"/auth/register", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Content-Length': JSONString.length
                },
                body: JSONString
            }).then(function (response) {
                switch (response.status) {
                    case 409:
                        resultString.style.display = "inline";
                        resultString.innerText = registerError;
                        break;
                    case 302:
                        window.location = "/login";
                        break;
                }
            })
        } catch (error) {
            console.error("Error: " + error);
        }
    })
})