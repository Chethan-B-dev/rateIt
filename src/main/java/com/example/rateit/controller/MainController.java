package com.example.rateit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * created by chethan on 19-12-2021
 **/
@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping
    public String home(){
        return "index";
    }
}

