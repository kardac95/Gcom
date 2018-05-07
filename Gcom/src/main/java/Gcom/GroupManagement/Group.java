package Gcom.GroupManagement;

import java.io.Serializable;
import java.util.*;

public class Group implements Serializable{
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

    public Member[] getMembers() {
        Set keySet = members.keySet();
        Member[] kalleList = new Member[members.size() + 1];
        int i = 0;
        for (Object member : keySet) {
            kalleList[i] = members.get(member);
            i++;
        }

        return kalleList;
    }

    public Member getMember(String name) {
        return members.get(name);
    }

    public boolean isMember(String memberName) {
        return members.keySet().contains(memberName);
    }

    public String getName() {
        return name;
    }
}
