package gcom.groupmanagement;

import gcom.Message;

import gcom.debugger.Debug;
import gcom.messageordering.Ordering;
import gcom.messageordering.OrderingObject;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GroupManager {
    private Queue<Message> incomingQueue;
    private HashMap<String, Group> groups;
    private Ordering order;
    public Queue<Message> outgoingQueue;
    private Thread monitorOutThread;
    private Member me;

    public GroupManager(Member me) {
        this.groups = new HashMap<>();
        this.outgoingQueue = new LinkedBlockingQueue<>();
        this.incomingQueue = new LinkedBlockingQueue<>();
        this.me = me;
        initOrdering(me);
    }

    public void initOrdering(Member myInfo) {
        order = new OrderingObject();
        order.initOrdering(myInfo);
        monitorOutThread = new Thread(() -> {
            while(true) {
                Message m = null;
                try {
                    m = order.getOutMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                messageTypeAction(m);
                outgoingQueue.add(m);
            }
        });
        monitorOutThread.start();
    }

    private void messageTypeAction(Message m) {
        switch(m.getType()) {
            case "connect":
                groups.get(m.getGroup().getName()).addMember(m.getSender());
                order.addInQueue(new Message(
                        groups.get(m.getGroup().getName()),
                        me,
                        m.getSender().getName() + " has joined the group!",
                        "join",
                        null));
                break;
            case "join":
                if (groups.get(m.getGroup().getName()) == null) {
                    groups.put(m.getGroup().getName(), m.getGroup());
                } else {
                    groups.get(m.getGroup().getName()).setMembers(m.getGroup().getMembers());
                }
                break;
            case "message":
                break;
            case "disconnect":
                try {
                    groups.get(m.getGroup().getName()).removeMember(m.getSender().getName());
                } catch(NullPointerException e) {
                    //like I care
                }
                break;
            default:
                System.err.println("Unknown message type");
                break;
        }
    }

    public void createGroup(String groupName) {
        groups.put(groupName, new Group(groupName));
        groups.get(groupName).addMember(me);
    }

    public void joinGroupRequest(Member recipient, String groupName) {
        Group group = new Group(groupName);
        group.addMember(recipient);
        order.addInQueue(new Message(group, me, groupName, "connect", null));
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public Group[] getGroups() {
        Set keySet = groups.keySet();
        Group[] groupList = new Group[groups.size()];
        int i = 0;
        for (Object group : keySet) {
            groupList[i] = groups.get(group.toString());
            i++;
        }
        return groupList;
    }

    public Queue<Message> getOutgoingQueue() {
        return outgoingQueue;
    }

    public void messageGroup(String message, Member sender, String groupName) {
        order.addInQueue(new Message(
                groups.get(groupName),
                sender,
                message,
                "message",
                null));
    }

    public void leaveGroup(String groupName) {
        Group group = groups.get(groupName);
        order.addInQueue(new Message(
                group,
                group.getMember(me.getName()),
                me.getName() + " has disconnected from the group!", "disconnect",
                null));
        group.removeMember(me.getName());
    }


    public Debug getDebugger() {
        return order.getDebug();
    }

    public void createGroup(String groupName, String order) {
        this.groups.put(groupName, new Group(groupName));
        this.groups.get(groupName).addMember(me);
        this.groups.get(groupName).setOrder(order);
        this.order.addInQueue(new Message(
                                this.groups.get(groupName),
                                me,
                                "",
                                "creategroup",
                                null
        ) );
    }
}
