package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.Feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback/")
public class FeedbackCtrl {

    @Autowired
    FeedbackService feedbackService;

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

    /*
    获得自己的相关feedback
    SaResult getFeedbackUnread(long userId);
    SaResult getAllMyFeedback(long userId);
    SaResult readFeedback(long feedbackId);
     */
    @RequestMapping(value ="edit/getFeedbackUnread", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getFeedbackUnread(){
         return feedbackService.getFeedbackUnread(StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="edit/getAllMyFeedback", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getAllMyFeedback(){
        return feedbackService.getAllMyFeedback(StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="edit/readFeedback", method= RequestMethod.GET)
    @ResponseBody
    public SaResult readFeedback(long feedbackId){
        return feedbackService.readFeedback(feedbackId);
    }

}
