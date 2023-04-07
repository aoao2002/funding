package com.example.funding.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group/")
public class GroupCtrl {

    @RequestMapping(value ="view/getAllGroups", method= RequestMethod.POST)
    @ResponseBody
    public boolean getAllGroups(){
        // TODO
        // 1. get all groups
        return false;
    }


    /**
     * staff's behavior
     */

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


    /**
     * manager's behavior
     */

    @RequestMapping(value ="edit/modifyGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean modifyGroup(){
        // TODO
        // 1. check if name exists
        // 2. if exists, update database
        return false;
    }


    /**
     * president's behavior
     */

    @RequestMapping(value ="edit/createGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean createGroup(){
        // TODO
        // 1. check if name exists
        // 2. if not, insert into database
        return false;
    }
    @RequestMapping(value ="edit/deleteGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean deleteGroup(){
        // TODO
        // 1. check if name exists
        // 2. if exists, delete from database
        return false;
    }
    @RequestMapping(value ="edit/assignManager", method= RequestMethod.POST)
    @ResponseBody
    public boolean assignManager(){
        // TODO
        // 1. check if name exists
        // 2. if exists, update database
        return false;
    }
}
