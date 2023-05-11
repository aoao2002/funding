package com.example.funding.service.Application;

import org.springframework.stereotype.Service;

@Service
public interface ApplicationService {

    public AppInfo getAppInfoByNumber(String expendNumber, long staffId);
    public boolean submitApplication(AppInfo appInfo);
}
