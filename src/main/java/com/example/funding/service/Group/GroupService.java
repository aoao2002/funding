package com.example.funding.service.Group;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface GroupService {
    public List<GroupInfo> getAllGroups();
    public boolean joinGroup(String groupName, long staffId);
    public boolean quitGroup(String groupName, long staffId);
    public boolean createGroup(String groupName, long staffId);
    public boolean deleteGroup(String groupName, long staffId);
    public boolean assignManager(String groupName, String manEmail, long staffId);
}
