import {createRouter, createWebHistory} from "vue-router";
import Contacts from "../vue/Contacts.vue";
import Login from "../vue/Login.vue";
import Registration from "../vue/Registration.vue";
import Admin from "../vue/Admin.vue";

const routes = [
    {path: "/", component: Contacts},
    {path: "/login", component: Login},
    {path: "/registration", component: Registration},
    {path: "/admin", component: Admin},
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;