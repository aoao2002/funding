package com.example.funding.service.Feedback;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.User;
import com.example.funding.dao.FeedbackDao;
import com.example.funding.dao.UserDao;
import com.example.funding.service.Application.FeedbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedbackMpl implements FeedbackService{

    @Autowired
    UserDao userDao;
    @Autowired
    FeedbackDao feedbackDao;
    @Override
    public SaResult getFeedbackUnread(long userId) {
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error("this user is not exist.");
        }
        return SaResult.ok().setData(user.get().getFeedbacks().stream()
                        .filter(s->s.getRead()==0)
                .map(FeedbackInfo::new).toList());
    }

    @Override
    public SaResult getAllMyFeedback(long userId) {
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error("this user is not exist.");
        }
        return SaResult.ok().setData(user.get().getFeedbacks().stream()
                .map(FeedbackInfo::new).toList());
    }

    @Override
    public SaResult readFeedback(long feedbackId) {
        feedbackDao.updateReadById(1, feedbackId);
        return SaResult.ok();
    }
}
