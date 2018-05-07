package Gcom.GroupManagement;

import Gcom.Message;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class GroupManager {
    private ArrayBlockingQueue<Message> incomingQueue;
    private HashMap<String, Group> groups;
    private ArrayBlockingQueue<Message> outgoingQueue;


    public GroupManager() {
        this.groups = new HashMap<>();
        this.outgoingQueue = new ArrayBlockingQueue<>(10);
        this.incomingQueue = new ArrayBlockingQueue<>(20);
    }

    public void createGroup(String groupName) {
        groups.put(groupName, new Group(groupName));
    }
    public void createGroup(String groupName, HashMap<String, Member> members) {
        groups.put(groupName, new Group(groupName, members));
    }

    public void joinGroup(Group group, Member member) {
        outgoingQueue.add(new Message(group, member, null, "connect", null));
        group.addMember(member);
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public Set getGroups() {
        return groups.keySet();
    }

    public void messageGroup(String message, Member sender) {

    }

    public void getAvailableGroups() {
        //connect to nameserver

        //request list of groups

        //show groups and connection alternatives
    }
}
