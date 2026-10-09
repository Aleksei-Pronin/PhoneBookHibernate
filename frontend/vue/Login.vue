<template>
    <div class="container py-4">
        <h1 class="text-center mb-4">Телефонная книга</h1>

        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h2 class="card-title mb-4 text-center fs-5">Вход</h2>

                        <form @submit.prevent="login" novalidate autocomplete="off">
                            <div class="mb-3">
                                <label for="userName" class="form-label">Логин</label>
                                <input v-model.trim="userName" type="text" class="form-control"
                                       :class="{ 'is-invalid': isUserNameInvalid }" id="userName"
                                       placeholder="Логин">
                                <div v-if="isUserNameInvalid" class="text-danger small mt-1">
                                    Необходимо заполнить поле
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label">Пароль</label>
                                <input v-model="password" type="password" class="form-control"
                                       :class="{ 'is-invalid': isPasswordInvalid }" id="password"
                                       placeholder="Пароль">
                                <div v-if="isPasswordInvalid" class="text-danger small mt-1">
                                    Необходимо заполнить поле
                                </div>
                            </div>

                            <button class="btn btn-primary w-100" type="submit" title="Войти">
                                <i class="bi bi-box-arrow-in-right me-1"></i>
                                Войти
                            </button>
                        </form>

                        <hr class="my-4">

                        <p class="text-center mb-0">
                            Нет аккаунта?
                            <router-link to="/registration">Зарегистрироваться</router-link>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import AuthService from "../js/authService";
import Toastify from "toastify-js";

export default {
    name: "Login",

    data() {
        return {
            userName: "",
            password: "",
            isUserNameInvalid: false,
            isPasswordInvalid: false,
            service: new AuthService()
        };
    },

    methods: {
        showError(message) {
            Toastify({
                text: message,
                duration: 3000,
                gravity: "top",
                position: "right",
                close: true,
                className: "phonebook-toast"
            }).showToast();
        },

        async login() {
            this.isUserNameInvalid = !this.userName.trim();
            this.isPasswordInvalid = !this.password.trim();

            if (this.isUserNameInvalid || this.isPasswordInvalid) {
                return;
            }

            const response = await this.service.login(this.userName.trim(), this.password);

            if (!response.success) {
                this.showError(response.message);
                return;
            }

            this.$router.push("/");
        }
    }
};
</script>