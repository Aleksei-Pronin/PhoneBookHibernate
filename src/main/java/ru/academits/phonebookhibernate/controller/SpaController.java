package ru.academits.phonebookhibernate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    @GetMapping({"/login", "/registration", "/admin"})
    public String spa() {
        return "forward:/index.html";
    }
}