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
    @RequestMapping(value ="view/getFeedbackUnread", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getFeedbackUnread(){
         return feedbackService.getFeedbackUnread(StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="view/getAllMyFeedback", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getAllMyFeedback(){
        return feedbackService.getAllMyFeedback(StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="view/readFeedback", method= RequestMethod.GET)
    @ResponseBody
    public SaResult readFeedback(long feedbackId){
        return feedbackService.readFeedback(feedbackId);
    }

    @RequestMapping(value ="view/getFeedbackByAppID", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getFeedbackByAppID(long appID){
        return feedbackService.getFeedbackByAppID(appID,StpUtil.getLoginIdAsLong());
    }

}
