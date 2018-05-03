package GroupManagement;

import java.util.HashMap;

public class GroupManager {
    private HashMap<String, Group> groups;

    public GroupManager() {
        this.groups = new HashMap<>();
    }

    public void createGroup(String groupName) {
        groups.put(groupName, new Group(groupName));
    }
    public void createGroup(String groupName, HashMap<String, Member> members) {
        groups.put(groupName, new Group(groupName, members));
    }

    public void joinGroup(String groupName, Member member) {
        groups.get(groupName).addMember(member);
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public void getAvailableGroups() {
        //connect to nameserver

        //request list of groups

        //show groups and connection alternatives
    }
}
