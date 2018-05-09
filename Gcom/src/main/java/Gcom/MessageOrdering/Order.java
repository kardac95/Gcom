package Gcom.MessageOrdering;

import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.Communication;
import Gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Order {
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;
    private Queue<Message> outgoingQueue;
    private Queue<Message> incomingQueue;
    private Communication comm;

    private Thread inQueueMonitor;
    private Thread outQueueMonitor;





    public Order(Member myInfo) {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.incomingQueue = new LinkedBlockingQueue<>();

        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = new LinkedBlockingQueue<>();

        comm.getInQueue();
        outQueueMonitor = new Thread(() -> {
            while(true) {
                Message m = null;
                try {
                    m = ((LinkedBlockingQueue<Message>)comm.getInQueue()).take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(m.getType().equals("join")) {
                    comm.connectToMembers(m.getGroup().getMembers());
                }
                System.out.println("Order outQueue type: " + m.getType());
                System.out.println("Order outQueue message: " + m.getMessage());
                outgoingQueue.add(m);
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
                System.out.println("Order Down");
                System.out.println("Order inQueue type: " + m.getType());
                System.out.println("Order inQueue message: " + m.getMessage());
                if(m.getGroup() != null) {
                    System.out.println(m.getGroup().getMembers().length);
                    Arrays.stream(m.getGroup().getMembers()).forEach(i -> {
                        System.out.println("Order inQueue Members name: " + i.getName());
                        System.out.println("Order inQueue Members address: " + i.getAddress());
                        System.out.println("Order inQueue Members port: " + i.getPort());
                    });
                }
                if(m.getType().equals("join")) {
                    comm.connectToMembers(m.getGroup().getMembers());
                }
                send(m);
            }

        });
        inQueueMonitor.start();
        outQueueMonitor.start();
    }

    public void send(Message message){
        if(message.getType().equals("connect")) {
            message.setVectorClock(vectorClock);
            comm.connectToMember(message.getRecipient());
            comm.unReliableUnicast(message, message.getRecipient());
        } else{
            vectorClock.set(memberIndex, vectorClock.get(memberIndex) + 1);
            message.setVectorClock(vectorClock);
            comm.unReliableMulticast(message, message.getGroup().getMembers());
        }
    }

    public void setVectorClock(ArrayList<Integer> vectorClock, int memberIndex){
        this.vectorClock = vectorClock;
        this.memberIndex= memberIndex;
    }

    public void incrementVectorClock(int index) {
        vectorClock.set(index, vectorClock.get(index) + 1);
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

    public Message getNextIncommingMessage() throws InterruptedException {
        return ((LinkedBlockingQueue<Message>)incomingQueue).take();
    }
}

