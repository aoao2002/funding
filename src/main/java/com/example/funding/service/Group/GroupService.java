package com.example.funding.service.Group;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.Application.GroupAppInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public interface GroupService {
    public List<GroupInfo> getAllGroups();
    public SaResult applyGroup(String groupName, String comment, long staffId);
    public SaResult tempSaveGroup(String groupName, String comment, long staffId);
    public SaResult getMyGroupApplication(long staffId);
    public SaResult getMyGroupAppToExam(long managerId);
    public boolean passApplyGroup(long applyId);
    public boolean rejectApplyGroup(long applyId);
    public Set<GroupInfo> getMyGroups(long staffId);

    public boolean joinGroup(String groupName, long staffId);
    public boolean quitGroup(String groupName, long staffId);
    public Set<GroupAppInfo> getAllGroupApplicationToBeChecked(long staffId);
    public boolean createGroup(String groupName);
    public boolean deleteGroup(String groupName);
    public boolean assignManager(String groupName, String manEmail);
    public boolean unassignManager(String groupName, String manEmail);
    public boolean assignStaff(String groupName, String manEmail);
    public boolean unassignStaff(String groupName, String manEmail);

    public boolean checkGroupEditPower(String applyID, String groupName, String UserID);
}
