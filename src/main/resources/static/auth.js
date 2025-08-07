// ==============================
// 🟢 Store JWT in cookie after login
// ==============================
function loginAndStoreToken() {
    const username = document.getElementById("login-username").value;
    const password = document.getElementById("login-password").value;

    fetch('/identity/token', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
        .then(res => res.json())
        .then(data => {
            const token = data.Data?.token;
            const refreshToken = data.Data?.refresh;

            if (!token) throw new Error("Token not found");

            // Store tokens in cookie
            document.cookie = `jwt=${token}; path=/; max-age=3600; secure; samesite=strict`;
            document.cookie = `refreshToken=${refreshToken}; path=/; max-age=86400; secure; samesite=strict`;

            alert("Login successful!");
        })
        .catch(err => {
            alert("Login error: " + err.message);
        });
}

// ==============================
// 🟡 Read cookie helper
// ==============================
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

// ==============================
// 🔵 Generic API call function with JWT header
// ==============================
async function callApiWithJwt(method, url, body = null) {
    const token = getCookie("jwt");
    if (!token) {
        alert("JWT not found. Please login first.");
        return;
    }

    const options = {
        method: method,
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const res = await fetch(url, options);
        if (!res.ok) throw new Error(`API call failed with status ${res.status}`);
        return await res.json();
    } catch (err) {
        alert("Error: " + err.message);
        console.error(err);
    }
}

// ==============================
// 🔴 Save patient data via API call
// ==============================
function savePatient() {
    const patientData = {
        name: document.getElementById("form:name").value,
        age: document.getElementById("form:age").value,
        gender: document.getElementById("form:gender").value,
        address: document.getElementById("form:address").value,
        bloodGroup: document.getElementById("form:blood").value,
        medicalHistory: document.getElementById("form:history").value
    };

    callApiWithJwt("POST", "/patient/save", patientData)
        .then(response => {
            if (response) {
                alert("Patient saved successfully!");
                // Optionally reset form
            }
        });
}
