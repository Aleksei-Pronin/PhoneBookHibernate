import axios from "axios";

export default class AuthService {
    login(userName, password) {
        const body = new URLSearchParams();
        body.append("username", userName);
        body.append("password", password);
        return axios.post("/login", body, {
            headers: {"Content-Type": "application/x-www-form-urlencoded"}
        })
            .then(response => response.data)
            .catch(error => {
                if (error.response) {
                    return error.response.data;
                }

                throw error;
            });
    }

    register(userName, password) {
        return axios.post("/registration", {userName, password})
            .then(response => response.data);
    }

    logout() {
        return axios.post("/logout")
            .then(response => response.data);
    }
}