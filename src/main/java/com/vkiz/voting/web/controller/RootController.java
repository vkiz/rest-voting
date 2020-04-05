package com.vkiz.voting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {

    @GetMapping("/")
    @ResponseBody
    public String root() {
        return "Welcome to Restaurant voting application";
    }
}
