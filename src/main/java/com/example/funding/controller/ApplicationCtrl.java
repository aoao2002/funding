package com.example.funding.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application/")
public class ApplicationCtrl {
    @RequestMapping(value ="edit/submitApplication", method= RequestMethod.POST)
    @ResponseBody
    public boolean submitApplication(){
        // TODO
        return false;
    }
    @RequestMapping(value ="edit/withdrawApplication", method= RequestMethod.POST)
    @ResponseBody
    public boolean withdrawApplication(){
        // TODO
        return false;
    }

    @RequestMapping(value ="edit/newExpenditureApplication", method= RequestMethod.POST)
    @ResponseBody
    public boolean newExpenditureApplication(){
        // TODO
        return false;
    }
}
