package com.example.funding.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback/")
public class FeedbackCtrl {
    @RequestMapping(value ="edit/replyNewExpenditure", method= RequestMethod.POST)
    @ResponseBody
    public boolean replyNewExpenditure(){
        // TODO
        return false;
    }
    @RequestMapping(value ="edit/replyApplyExpenditure", method= RequestMethod.POST)
    @ResponseBody
    public boolean replyApplyExpenditure(){
        // TODO
        return false;
    }
}
