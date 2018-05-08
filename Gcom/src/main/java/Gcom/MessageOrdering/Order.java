package Gcom.MessageOrdering;

import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.Communication;
import Gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
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

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
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
        outQueueMonitor = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    lock.lock();
                    try {
                        while(comm.getInQueue().isEmpty()) {
                            condition.await();
                        }
                    } catch (InterruptedException ignored) {
                    } finally {
                        lock.unlock();
                    }
                    System.out.println("Order Down");
                    Message m = comm.getInQueue().poll();
                    if(m.getType().equals("join")) {
                        comm.connectToMembers(m.getGroup().getMembers());
                    }
                    System.out.println(m.getMessage());
                    outgoingQueue.add(m);
                }
            }
        });

        inQueueMonitor = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    lock.lock();
                    try {
                        while(incomingQueue.isEmpty()) {
                            condition.await();
                        }
                    } catch (InterruptedException ignored) {
                    } finally {
                        lock.unlock();
                    }
                    System.out.println("Order Down");
                    Message m = incomingQueue.poll();
                    System.out.println(m.getMessage());
                    send(m);
                }

            }
        });
        inQueueMonitor.start();
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

    public Message getNextOutgoingMessage() {
        return outgoingQueue.poll();
    }

    public void addNextIncomingMessage(Message message) {
        incomingQueue.add(message);
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    protected boolean outQueueIsEmpty() {
        return outgoingQueue.isEmpty();
    }

    public Message getNextIncommingMessage() {
        return incomingQueue.poll();
    }
}

