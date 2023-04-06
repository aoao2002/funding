package com.example.funding.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenditure/")
public class ExpenditureCtrl {
    // 展示经费使用情况 还有很多种展示方式
    @RequestMapping(value ="edit/showExpenditure", method= RequestMethod.POST)
    @ResponseBody
    public boolean showExpenditure(){
        // TODO
        return false;
    }
}
