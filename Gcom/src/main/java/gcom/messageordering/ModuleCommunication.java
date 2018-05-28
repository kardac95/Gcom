package gcom.messageordering;

import gcom.Message;
import gcom.communication.CommunicationObject;
import gcom.debugger.Debug;
import gcom.debugger.DebugObject;
import gcom.groupmanagement.Member;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ModuleCommunication {
    //private Order order;
    private ConcurrentMap<String, Order> groupOrders;
    private Integer memberIndex;
    private Queue<Message> outgoingQueue;
    private Queue<Message> incomingQueue;
    private CommunicationObject comm;
    private Debug debugger;
    private Thread inQueueMonitor;
    private Thread outQueueMonitor;
    private Member myInfo;

    public ModuleCommunication(Member myInfo) {
        this.myInfo = myInfo;
        this.groupOrders = new ConcurrentHashMap<>();
        this.memberIndex = 0;
        this.incomingQueue = new LinkedBlockingQueue<>();
        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = new LinkedBlockingQueue<>();
       // this.order = new CausalOrder(myInfo.getAddress()+myInfo.getPort());
        //this.order = new Unorderd();
        debugger = new DebugObject(comm);

        outQueueMonitor = new Thread(() -> {
            while(true) {
                Message m = debugger.getNextMessage();//comm.getNextMessage();
                //System.out.println("Receive queue");
                switch (m.getType()) {
                    case "join":
                        if(!groupOrders.containsKey(m.getGroup().getName())) {
                            setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                        }
                        comm.connectToMembers(m.getGroup().getMembers());
                        groupOrders.get(m.getGroup().getName()).getClock().addNewMemberClock(m.getGroup().getMembers(), m.getVectorClock());
                        //order.clock.addNewMemberClock(m.getGroup().getMembers(), m.getVectorClock());
                        break;
                    case "disconnect":
                        /* Disconnect sending member */
                        //comm.disconnectMember(m.getSender());
                        break;
                    default:
                        break;
                }
                groupOrders.get(m.getGroup().getName()).Ordering(m, outgoingQueue);
                //order.Ordering(m, outgoingQueue);
            }
        });

        inQueueMonitor = new Thread(() -> {
            while(true) {

                Message m = null;
                try {
                    m = ((LinkedBlockingQueue<Message>)incomingQueue).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(m.getType().equals("join")) {
                    comm.connectToMembers(m.getGroup().getMembers());
                    /*if(!groupOrders.containsKey(m.getGroup().getName())) {
                        setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                    }*/
                } else if(m.getType().equals("creategroup")) {
                    setOrder(m.getGroup().getName(), m.getGroup().getOrder());
                    continue;
                }else if(m.getType().equals("connect")) {
                    send(m);
                    continue;
                }
                send(groupOrders.get(m.getGroup().getName()).sendOrder(m));
                //m = order.sendOrder(m);

                //send(m);
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
                System.err.println("Trying to send null message");
            } else {
                comm.unReliableMulticast(message, message.getGroup().getMembers());
            }
        }
    }

    public void setOrder(String groupName,String order) {
        if(order.equals("causal")) {
            this.groupOrders.put(groupName, new CausalOrder(myInfo.getAddress()+myInfo.getPort()));//this.order.getClock().getMyId()));
        } else if(order.equals("unordered")) {
            this.groupOrders.put(groupName, new Unorderd());
        } else {
            System.err.println(order + "is not a valid order.");
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

