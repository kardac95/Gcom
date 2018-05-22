package gcom.messageordering;

import gcom.groupmanagement.Member;
import gcom.Message;
import gcom.communication.Communication;
import gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ModuleCommunication {
    private Order order;
    private Integer memberIndex;
    private Queue<Message> outgoingQueue;
    private Queue<Message> incomingQueue;
    private Communication comm;

    private Thread inQueueMonitor;
    private Thread outQueueMonitor;

    public ModuleCommunication(Member myInfo) {
        this.memberIndex = 0;
        this.incomingQueue = new LinkedBlockingQueue<>();
        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = new LinkedBlockingQueue<>();
        this.order = new CausalOrder(myInfo.getAddress()+myInfo.getPort());
        //this.order = new Unorderd();

        outQueueMonitor = new Thread(() -> {
            while(true) {
                /*Message m = null;
                try {
                    m = ((LinkedBlockingQueue<Message>)comm.getInQueue()).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                Message m = comm.getNextMessage();
                if(m == null) {
                    System.err.println("Received message is null");
                    continue;
                }
                System.out.println("Receive queue");
                if(m.getType().equals("join")) {
                    comm.connectToMembers(m.getGroup().getMembers());
                    order.clock.aidsMethod(m.getGroup().getMembers());
                } else if(m.getType().equals("disconnect")) {
                    /* Disconnect sending member */
                    //comm.disconnectMember(m.getSender());
                }
                /*
                System.out.println("ModuleCommunication outQueue type: " + m.getType());
                System.out.println("ModuleCommunication outQueue message: " + m.getMessage());
                */
                order.Ordering(m, outgoingQueue);
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

                //System.out.println("Outgoing queue");

                //System.out.println("ModuleCommunication Down");
                System.out.println("ModuleCommunication inQueue type: " + m.getType());
                System.out.println("ModuleCommunication inQueue message: " + m.getMessage());
                */
                if(m.getGroup() != null) {
                    System.out.println("Group members: " + m.getGroup().getMembers().length);
                    /*
                    Arrays.stream(m.getGroup().getMembers()).forEach(i -> {
                        System.out.println("ModuleCommunication inQueue Members name: " + i.getName());
                        System.out.println("ModuleCommunication inQueue Members address: " + i.getAddress());
                        System.out.println("ModuleCommunication inQueue Members port: " + i.getPort());
                    });*/
                } else {
                    System.err.println("Group is null in queue monitor");
                }
                if(m.getType().equals("join")) {
                    comm.connectToMembers(m.getGroup().getMembers());
                }
                if(m.getType().equals("message")) {
                    order.clock.inc();
                    m.setVectorClock(order.clock);
                }
                System.out.println("Message Type: " + m.getType());
                send(m);
            }

        });
        inQueueMonitor.start();
        outQueueMonitor.start();
    }

    public void send(Message message){
        if(message.getType().equals("connect")) {

            comm.connectToMember(message.getRecipient());
            comm.unReliableUnicast(message, message.getRecipient());

        } else{
            if(message.getGroup() == null) {
                System.err.println("Trying to send null message");
            } else {
                comm.unReliableMulticast(message, message.getGroup().getMembers());
            }
        }
    }

    public void setOrder(String order) {
        if(order.equals("causal")) {
            this.order = new CausalOrder(this.order.clock.getMyId());
        } else if(order.equals("unordered")) {
            this.order = new Unorderd();
        } else {
            System.err.println(order + "is not a valid order.");
        }
    }

    public Message getNextIncommingMessage() throws InterruptedException {
        return ((LinkedBlockingQueue<Message>)incomingQueue).take();
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

