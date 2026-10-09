import {createApp} from "vue";
import PhoneBook from "../vue/PhoneBook.vue";
import router from "../router";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "toastify-js/src/toastify.css";
import "../scss/style.scss";

const app = createApp(PhoneBook);

app.use(router);
app.mount("#app");