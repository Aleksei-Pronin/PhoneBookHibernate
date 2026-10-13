import {createApp} from "vue";
import axios from "axios";
import PhoneBook from "../vue/PhoneBook.vue";
import router from "../router";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "toastify-js/src/toastify.css";
import "../scss/style.scss";

axios.defaults.xsrfCookieName = "XSRF-TOKEN";
axios.defaults.xsrfHeaderName = "X-XSRF-TOKEN";
axios.defaults.withXSRFToken = true;

axios.get("/csrf")
    .then(() => {
        const app = createApp(PhoneBook);

        app.use(router);
        app.mount("#app");
    })
    .catch(error => {
        console.error("Failed to initialize CSRF token", error);
    });