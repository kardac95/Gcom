package gcom.messageordering;

import gcom.Message;
import gcom.communication.CommunicationObject;
import gcom.debugger.Debug;
import gcom.debugger.DebugObject;
import gcom.groupmanagement.Member;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ModuleCommunication {
    private ConcurrentMap<String, Order> groupOrders;
    private Queue<Message> outgoingQueue;
    private Queue<Message> incomingQueue;
    private CommunicationObject comm;
    private Debug debugger;
    private Member myInfo;

    public ModuleCommunication(Member myInfo) {
        Thread inQueueMonitor;
        Thread outQueueMonitor;
        this.myInfo = myInfo;
        this.groupOrders = new ConcurrentHashMap<>();
        this.incomingQueue = new LinkedBlockingQueue<>();
        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = new LinkedBlockingQueue<>();
        debugger = new DebugObject(comm);

        outQueueMonitor = new Thread(() -> {
            while(true) {
                Message m = debugger.getNextMessage();
                switch (m.getType()) {
                    case "join":
                        if(!groupOrders.containsKey(m.getGroup().getName())) {
                            setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                        }
                        comm.connectToMembers(m.getGroup().getMembers());
                        groupOrders.get(m.getGroup().getName()).getClock().addNewMemberClock(m.getGroup().getMembers(), m.getVectorClock());
                        break;
                    case "disconnect":
                        /* Disconnect sending member */
                        comm.disconnectMember(m.getSender());
                        break;
                    default:
                        break;
                }

                if(groupOrders.containsKey(m.getGroup().getName())) {
                    continue;
                }

                groupOrders.get(m.getGroup().getName()).Ordering(m, outgoingQueue);
                debugger.setOrderBuffer(m.getGroup().getName(), groupOrders.get(m.getGroup().getName()).getBuffer());

            }
        });

        inQueueMonitor = new Thread(() -> {
            while(true) {

                Message m;
                try {
                    m = ((LinkedBlockingQueue<Message>)incomingQueue).take();
                } catch (InterruptedException e) {
                    System.err.println("Message take failed from in'Queue.");
                    continue;
                }
                switch (m.getType()) {
                    case "join":
                        comm.connectToMembers(m.getGroup().getMembers());
                        /*if(!groupOrders.containsKey(m.getGroup().getName())) {
                            setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                        }*/
                        break;
                    case "creategroup":
                        setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                        continue;
                    case "connect":
                        send(m);
                        continue;
                }

                send(groupOrders.get(m.getGroup().getName()).sendOrder(m));

                if(m.getType().equals("disconnect")) {
                    if(m.getSender().getName().equals(myInfo.getName())) {
                        groupOrders.remove(m.getGroup().getName());
                    }
                }
            }

        });
        inQueueMonitor.start();
        outQueueMonitor.start();
    }

    public void send(Message message){
        if(message.getType().equals("connect")) {

            comm.connectToMember(message.getGroup().getMembers()[0]);
            comm.unReliableUnicast(message, message.getGroup().getMembers()[0]);

        } else{
            if(message.getGroup() == null) {
                System.err.println("No specified recipient.");
            } else {
                comm.unReliableMulticast(message, message.getGroup().getMembers());
            }
        }
    }

    public void setOrder(String groupName,String order) {
        switch (order) {
            case "causal":
                this.groupOrders.put(groupName, new CausalOrder(myInfo.getAddress() + myInfo.getPort()));//this.order.getClock().getMyId()));
                break;
            case "unordered":
                this.groupOrders.put(groupName, new Unordered(myInfo.getAddress() + myInfo.getPort()));
                break;
            default:
                System.err.println(order + "is not a valid order.");
                break;
        }
    }

    public Message getNextIncommingMessage() throws InterruptedException {
        return ((LinkedBlockingQueue<Message>)incomingQueue).take();
    }

    public Debug getDebugger() {
        return debugger;
    }

    public Message getNextOutgoingMessage() throws InterruptedException {
        return ((LinkedBlockingQueue<Message>)outgoingQueue).take();
    }

    public void addNextIncomingMessage(Message message) {
        incomingQueue.add(message);
    }

    protected boolean outQueueIsEmpty() {
        return outgoingQueue.isEmpty();
    }
}

