document.getElementById("logout-button").addEventListener('click', () => {
    document.cookie.split(";").forEach(function(c) { 
        document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
    });
    window.location = "/";
    localStorage.setItem("uid", "");
});