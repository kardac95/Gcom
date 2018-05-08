package Gcom.GroupManagement;

import Gcom.Message;
import Gcom.MessageOrdering.Ordering;
import Gcom.MessageOrdering.OrderingObject;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class GroupManager {
    private Queue<Message> incomingQueue;
    private HashMap<String, Group> groups;
    private Ordering order;
    public Queue<Message> outgoingQueue;
    private Thread monitorOutThread;


    public GroupManager() {
        this.groups = new HashMap<>();
        this.outgoingQueue = new LinkedBlockingQueue<>(10);
        this.incomingQueue = new LinkedBlockingQueue<>(20);
    }

    public void initOrdering(Member myInfo) {
        order = new OrderingObject();
        order.initOrdering(myInfo);
        monitorOutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Message m = null;
                    try {
                        m = order.getOutMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(m.getType().equals("connect")) {
                        groups.get(m.getMessage()).addMember(m.getSender());
                        m =  new Message(
                                groups.get(m.getMessage()),
                                m.getRecipient(),
                                "Faggot has joined the group!",
                                "join",
                                null);
                        order.addInQueue(m);
                    } else if(m.getType().equals("join")) {
                        groups.get(m.getGroup().getName()).setMembers(m.getGroup().getMembers());
                    }

                    System.out.println("Group manager receive");
                    System.out.println(m.getType());
                    System.out.println(m.getMessage());
                    outgoingQueue.add(m);

                    /*lock.lock();
                    try {
                        while(order.outQueueIsEmpty()) {
                            condition.await();
                        }
                    } catch (InterruptedException ignored) {
                    } finally {
                        lock.unlock();
                    }
                    Message m = order.getOutMessage();
                    if(m.getType().equals("connect")) {
                        groups.get(m.getMessage()).addMember(m.getSender());
                        order.addInQueue(
                                new Message(
                                        groups.get(m.getMessage()),
                                        m.getRecipient(),
                                        "Faggot has joined the group!",
                                        "join",
                                        null));
                    } else if(m.getType().equals("join")) {
                        groups.get(m.getGroup().getName()).setMembers(m.getGroup().getMembers());
                    }

                    System.out.println("Group manager receive");
                    System.out.println(m.getType());
                    System.out.println(m.getMessage());
                    outgoingQueue.add(m);*/
                }
            }
        });
        monitorOutThread.start();
    }

    public void createGroup(String groupName) {
        groups.put(groupName, new Group(groupName));
    }
    public void createGroup(String groupName, HashMap<String, Member> members) {
        groups.put(groupName, new Group(groupName, members));
    }

    public void joinGroupRequest(Member recipient, Member me, String groupName) {
        order.addInQueue(new Message(recipient, me, groupName, "connect", null));
        System.out.println("JOINGREQ");
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
        Group[] groupList = new Group[groups.size() + 1];
        int i = 0;
        for (Object group : keySet) {
            groupList[i] = groups.get(group.toString());
            i++;
        }
        return groupList;
    }

    public void messageGroup(String message, Member sender, String groupName) {
        order.addInQueue(new Message(groups.get(groupName), sender, message, "message", null));
    }

    public void getAvailableGroups() {
        //connect to nameserver

        //request list of groups

        //show groups and connection alternatives
    }
}
