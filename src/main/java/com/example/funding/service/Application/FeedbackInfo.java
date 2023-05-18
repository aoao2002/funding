package com.example.funding.service.Application;

import com.example.funding.bean.Feedback;
import com.example.funding.service.User.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackInfo {
    private long id;
    private String comment;
    private String replyDate;
    private UserInfo replier;
    private long applicationId;
    public FeedbackInfo(){}
    public FeedbackInfo(Feedback feedback){
        this.id = feedback.getId();
        this.comment = feedback.getComment();
        this.replyDate = feedback.getReplyTime().toString();
        this.replier = new UserInfo(feedback.getUser());
        this.applicationId = feedback.getApplicationId();
    }
}
