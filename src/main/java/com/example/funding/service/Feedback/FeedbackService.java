package com.example.funding.service.Feedback;

import cn.dev33.satoken.util.SaResult;
import org.springframework.stereotype.Service;

@Service
public interface FeedbackService {

    SaResult getFeedbackUnread(long userId);
    SaResult getAllMyFeedback(long userId);
    SaResult readFeedback(long feedbackId);

    SaResult getFeedbackByAppID(long appID,long userID);
}
