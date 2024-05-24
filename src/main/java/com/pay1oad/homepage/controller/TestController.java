package com.pay1oad.homepage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "sbb on load";
    }
}
