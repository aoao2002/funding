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
}
