package com.example.funding.service.Group;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface GroupService {
    public List<GroupInfo> getAllGroups();
    public boolean applyGroup(String groupName, String comment, long staffId);
    public boolean passApplyGroup(long applyId);
    public boolean rejectApplyGroup(long applyId);

    public boolean joinGroup(String groupName, long staffId);
    public boolean quitGroup(String groupName, long staffId);
    public boolean createGroup(String groupName);
    public boolean deleteGroup(String groupName);
    public boolean assignManager(String groupName, String manEmail);
    public boolean unassignManager(String groupName, String manEmail);
}
