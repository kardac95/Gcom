package GroupManagement;

import java.util.*;

public class Group implements Observer {
    private HashMap<String, Member> members;
    private String name;


    public Group(String name) {
        this.name = name;
        this.members = new HashMap<>();
    }

    public Group(String groupName, HashMap<String, Member> members) {
        this.name = groupName;
        this.members = members;
    }

    public void addMember(String memberName, String address, String port) {
        members.put(memberName, new Member(memberName, address, port));
    }
    public void addMember(Member member) {
        members.put(member.getName(), member);
    }

    public void removeMember(String memberName) {
        members.remove(memberName);
    }

    public Set getMembers() {
        return members.keySet();
    }

    public boolean isMember(String memberName) {
        return members.keySet().contains(memberName);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
