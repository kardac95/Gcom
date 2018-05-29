package gcom.groupmanagement;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Group implements Serializable{
    private Map<String, Member> members;
    private String name;
    private String order;

    public Group(String name) {
        this.name = name;
        this.members = new ConcurrentHashMap<>();
    }
    public Group(String groupName, ConcurrentHashMap<String, Member> members) {
        this.name = groupName;
        this.members = members;
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

    public String getName() {
        return name;
    }

    public void setMembers(Member[] members) {
        ((ConcurrentHashMap<String,Member>)this.members).forEach(members.length, (s, member) -> this.members.remove(s));

        Arrays.stream(members).forEach(member -> this.members.put(member.getName(), member));
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
