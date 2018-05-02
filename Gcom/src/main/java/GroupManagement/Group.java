package GroupManagement;

import java.util.HashMap;

public class Group {
    //variable:memberList
    private HashMap<String, Member> members;
    //variable:identifier
    private String name;

    public Group(String name) {
        this.name = name;
        this.members = new HashMap<>();
    }

    public Group(String name, HashMap<String, Member> members) {
        this.name = name;
        this.members = members;
    }

    //method:addMember
    public void addMember(String name, String address) {
        members.put(name, new Member(name, address));
    }

    //method:removeMember
    public void removeMember(String name) {
        members.remove(name);
    }

    //method:?joinGroup
    //method:?refreshMemberList
}
