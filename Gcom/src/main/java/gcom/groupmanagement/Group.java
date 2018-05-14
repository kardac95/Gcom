package gcom.groupmanagement;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Group implements Serializable{
    private Map<String, Member> members;
    private String name;

    public Group(String name) {
        this.name = name;
        this.members = new ConcurrentHashMap<>();
    }
    public Group(String groupName, ConcurrentHashMap<String, Member> members) {
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
        Member[] memberList = new Member[members.size()];
        int i = 0;
        for (Object member : keySet) {
            memberList[i] = members.get(member.toString());
            i++;
        }

        return memberList;
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

    public void setMembers(Member[] members) {
        /*Set keySet = this.members.keySet();
        keySet.forEach(m -> this.members.remove(m));

        Arrays.stream(members).forEach(m -> {
            this.members.put(m.getName(), m);
        });
        */
        ((ConcurrentHashMap<String,Member>)this.members).forEach(members.length, (s, member) -> {
            this.members.remove(s);
        });

        Arrays.stream(members).forEach(member -> {
            ((ConcurrentHashMap<String,Member>)this.members).put(member.getName(), member);
        });

    }
}
