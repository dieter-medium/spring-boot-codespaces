package com.example.teama.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToBlogPosts() {
        return "redirect:/blog-posts";
    }
}