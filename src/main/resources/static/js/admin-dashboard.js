/**
 * Fetches the api endpoint responsible
 *  for changing the proxy type
 * @param {String} type The proxy type
 * @param {Number} uid  The uid of the user
 */
function fetchProxyType(type, uid) {
    const typeData = `{"uid":"${uid}","type":"${type}"}`;
    fetch(apiHost+"/set-proxy-type", {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': typeData.length
        },
        body: typeData
    })
}

/**
 * Fetches the api endpoint responsible
 *  for changing on / off proxy authentication
 * @param {String} state True or False
 * @param {Number} uid The uid of the user
 */
function fetchProxyAuth(state, uid) {
    const typeData = `{"uid":"${uid}","state":"${state}"}`;
    fetch(apiHost+"/set-proxy-auth", {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': typeData.length
        },
        body: typeData
    })
}

document.addEventListener('DOMContentLoaded', async function () {
    try {
        /**************************
         * FETCHES FOR GLOBAL STATS
         *************************/
        // Get fields 
        const totalUsersField = document.getElementById("totalUsersField");
        const totalConnsField = document.getElementById("totalConnsField");
        // Default values
        totalUsersField.innerText = 0;
        totalConnsField.innerText = 0;
        // Data uid
        const data = `{"uid":"${localStorage.getItem("uid")}"}`;

        setInterval(async () => {
            await fetch(apiHost+"/get-total-users", {
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
                totalUsersField.innerText = data.value;
            });
            await fetch(apiHost+"/get-total-conns", {
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
                totalConnsField.innerText = data.value;
            });
        },3000);

        /***************
         * CONFIGURATION
         **************/
        // Get submit buttons
        const proxyTypeSubmit = document.getElementById("proxy-type-submit");
        const proxyAuthSubmit = document.getElementById("proxy-auth-submit");
        // Get input radio
        const defaultProxy = document.getElementById("proxy-type-1");
        const otherProxy = document.getElementById("proxy-type-2");
        const authEnable = document.getElementById("proxy-auth-enable");

        proxyTypeSubmit.addEventListener('click', () => {
            if (defaultProxy.checked) {
                fetchProxyType("FORWARD", localStorage.getItem("uid"));
            }
            if(otherProxy.checked) {
                console.log("Not implemented");
            }
        });

        proxyAuthSubmit.addEventListener('click', () => {
            if (authEnable.checked) {
                fetchProxyAuth("true", localStorage.getItem("uid"));
            } else {
                fetchProxyAuth("false", localStorage.getItem("uid"));
            }
        })


    } catch (error) {
        console.error("Error" + error);
    }
})