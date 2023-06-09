package com.example.funding.service.Feedback;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Application;
import com.example.funding.bean.Feedback;
import com.example.funding.bean.User;
import com.example.funding.dao.ApplicationDao;
import com.example.funding.dao.FeedbackDao;
import com.example.funding.dao.UserDao;
import com.example.funding.service.Application.FeedbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FeedbackServiceMpl implements FeedbackService{

    @Autowired
    UserDao userDao;
    @Autowired
    FeedbackDao feedbackDao;
    @Autowired
    private ApplicationDao applicationDao;

    @Override
    public SaResult getFeedbackUnread(long userId) {
//        Optional<User> user = userDao.findById(userId);
        User user = userDao.findByUserId(userId);
        if (user==null){
            return SaResult.error("this user is not exist.");
        }
        return SaResult.ok().setData(user.getFeedbacks().stream()
                        .filter(s->s.getRead()==0)
                .map(FeedbackInfo::new).toList());
    }

    @Override
    public SaResult getAllMyFeedback(long userId) {
//        Optional<User> user = userDao.findById(userId);
        User user = userDao.findByUserId(userId);
        if (user==null){
            return SaResult.error("this user is not exist.");
        }
        return SaResult.ok().setData(user.getFeedbacks().stream()
                .map(FeedbackInfo::new).toList());
    }

    @Override
    public SaResult readFeedback(long feedbackId) {
        feedbackDao.updateReadById(1, feedbackId);
        return SaResult.ok();
    }

    @Override
    public SaResult getFeedbackByAppID(long appID,long userID){
//        Optional<User> user = userDao.findById(userID);
        User user = userDao.findByUserId(userID);
        if (user==null){
            return SaResult.error("this user is not exist.");
        }
        Application application = applicationDao.findById(appID);
        if (application == null){
            return SaResult.error("this application is not exist.");
        }
        //检查该user是否有权限查看该application的反馈
        if((!application.getUser().equals(user))&&user.getIdentity()==0){
            return SaResult.error("this user is not the person who apply this application.");
        }
        Feedback feedback = feedbackDao.findByApplicationId(appID);
        if(feedback==null){
            return SaResult.error("this application has no feedback.");
        }
        return SaResult.ok().setData(new FeedbackInfo(feedback));
    }
}
