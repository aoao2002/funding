package com.example.funding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StaffCtrl {
    @RequestMapping("/staff")
    @ResponseBody
    public String staff(){
        return "staff";
    }
}
