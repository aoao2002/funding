package com.example.funding.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group/")
public class GroupCtrl {
    @RequestMapping(value ="edit/joinGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean joinGroup(){
        // TODO
        // 1. check if name exists
        // 2. if not, insert into database
        return false;
    }
    @RequestMapping(value ="edit/quitGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean quitGroup(){
        // TODO
        // 1. check if name exists
        // 2. if exists, delete from database
        return false;
    }
}
