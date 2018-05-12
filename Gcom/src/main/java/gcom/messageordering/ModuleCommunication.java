package gcom.messageordering;

import gcom.groupmanagement.Member;
import gcom.Message;
import gcom.communication.Communication;
import gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ModuleCommunication {
    private Order order;
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;
    private Queue<Message> outgoingQueue;
    private Queue<Message> incomingQueue;
    private Communication comm;

    private Thread inQueueMonitor;
    private Thread outQueueMonitor;

    public ModuleCommunication(Member myInfo) {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.incomingQueue = new LinkedBlockingQueue<>();

        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = new LinkedBlockingQueue<>();
        this.order = new Unorderd();

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
                System.out.println("ModuleCommunication outQueue type: " + m.getType());
                System.out.println("ModuleCommunication outQueue message: " + m.getMessage());
                order.Ordering(m, outgoingQueue);
                //outgoingQueue.add(m);
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
                System.out.println("ModuleCommunication Down");
                System.out.println("ModuleCommunication inQueue type: " + m.getType());
                System.out.println("ModuleCommunication inQueue message: " + m.getMessage());
                if(m.getGroup() != null) {
                    System.out.println(m.getGroup().getMembers().length);
                    Arrays.stream(m.getGroup().getMembers()).forEach(i -> {
                        System.out.println("ModuleCommunication inQueue Members name: " + i.getName());
                        System.out.println("ModuleCommunication inQueue Members address: " + i.getAddress());
                        System.out.println("ModuleCommunication inQueue Members port: " + i.getPort());
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
            //message.setVectorClock(vectorClock);
            comm.connectToMember(message.getRecipient());
            comm.unReliableUnicast(message, message.getRecipient());
        } else{
            vectorClock.set(memberIndex, vectorClock.get(memberIndex) + 1);
            //message.setVectorClock(vectorClock);
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

