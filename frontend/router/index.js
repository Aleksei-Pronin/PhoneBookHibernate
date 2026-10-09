import {createRouter, createWebHistory} from "vue-router";
import Contacts from "../vue/Contacts.vue";
import Login from "../vue/Login.vue";
import Registration from "../vue/Registration.vue";

const routes = [
    {path: "/", component: Contacts},
    {path: "/login", component: Login},
    {path: "/registration", component: Registration},
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;