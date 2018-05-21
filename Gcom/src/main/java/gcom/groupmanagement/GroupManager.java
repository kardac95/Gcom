package gcom.groupmanagement;

import gcom.Message;
import gcom.messageordering.Ordering;
import gcom.messageordering.OrderingObject;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GroupManager {
    private Queue<Message> incomingQueue;
    private AtomicBoolean debug;
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
        this.debug = new AtomicBoolean(false);

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

                System.out.println("Group manager receive");
                System.out.println(m.getType());
                System.out.println(m.getMessage());
                outgoingQueue.add(m);
            }
        });
        monitorOutThread.start();
    }

    private void messageTypeAction(Message m) {
        switch(m.getType()) {
            case "connect":
                groups.get(m.getMessage()).addMember(m.getSender());
                order.addInQueue(new Message(
                        groups.get(m.getMessage()),
                        m.getRecipient(),
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
            default:
                System.err.println("Unknown message type");
        }
    }

    public void createGroup(String groupName) {
        groups.put(groupName, new Group(groupName));
        groups.get(groupName).addMember(me);
    }
    public void createGroup(String groupName, ConcurrentHashMap<String, Member> members) {
        groups.put(groupName, new Group(groupName, members));
    }

    public void joinGroupRequest(Member recipient, String groupName) {
        System.out.println("joinGroupRequest - GroupManager layer");
        order.addInQueue(new Message(recipient, me, groupName, "connect", null));
    }

    public void joinGroup(Group group, Member me) {
        order.addInQueue(new Message(group, me, null, "connect", null));
        group.addMember(me);
    }

    public void removeGroup(String groupName) {
        groups.remove(groupName);
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
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
        order.addInQueue(new Message(groups.get(groupName), sender, message, "message", null));
    }

    public void setDebug(Boolean debug) {
        this.debug.set(debug);
    }

    public Boolean isDebug() {
        return debug.get();
    }

    public void getAvailableGroups() {
        //connect to nameserver

        //request list of groups

        //show groups and connection alternatives
    }
}
