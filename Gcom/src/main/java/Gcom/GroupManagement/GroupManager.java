package Gcom.GroupManagement;

import Gcom.Message;
import Gcom.MessageOrdering.Ordering;
import Gcom.MessageOrdering.OrderingObject;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class GroupManager {
    private ArrayBlockingQueue<Object> incomingQueue;
    private HashMap<String, Group> groups;
    private ArrayBlockingQueue<Object> outgoingQueue;
    private Ordering order;


    public GroupManager(Member myInfo) {
        this.groups = new HashMap<>();
        this.outgoingQueue = new ArrayBlockingQueue<>(10);
        this.incomingQueue = new ArrayBlockingQueue<>(20);
        order = new OrderingObject();
        order.initOrdering(myInfo);
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

    public Set getGroups() {
        return groups.keySet();
    }

    public void getAvailableGroups() {
        //connect to nameserver

        //request list of groups

        //show groups and connection alternatives
    }
}
