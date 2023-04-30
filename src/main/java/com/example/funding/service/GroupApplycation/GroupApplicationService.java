package com.example.funding.service.GroupApplycation;

import com.example.funding.bean.GroupApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface GroupApplicationService {
    GroupApplication findById(long applyID);
}
