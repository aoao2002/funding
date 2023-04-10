package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application/")
public class ApplicationCtrl {
    @RequestMapping(value ="edit/submitApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult submitApplication(String expenditureNumber, String comment,double applyAmount){
        // TODO
        return ReturnHelper.returnBool(false);
    }
    @RequestMapping(value ="edit/withdrawApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult withdrawApplication(){
        // TODO
        return ReturnHelper.returnBool(false);
    }

    @RequestMapping(value ="edit/newExpenditureApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
                                              double expenditureTotalAmount, String BeginTime, String EndTime){
        // TODO
        return ReturnHelper.returnBool(false);
    }
}
