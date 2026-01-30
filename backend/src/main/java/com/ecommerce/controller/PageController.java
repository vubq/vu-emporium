package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // This controller handles frontend routing
    // All non-API routes are forwarded to index.html by WebConfig

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
