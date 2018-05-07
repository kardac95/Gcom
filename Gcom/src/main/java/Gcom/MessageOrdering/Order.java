package Gcom.MessageOrdering;

import Gcom.GroupManagement.Group;
import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.Communication;
import Gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Order {
    private ArrayList<Integer> vectorClock;
    private Integer memberIndex;
    private ArrayBlockingQueue<Message> outgoingQueue;
    private ArrayBlockingQueue<Message> incomingQueue;
    private Communication comm;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Thread inQueueMonitor;




    public Order(Member myInfo) {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.outgoingQueue = new ArrayBlockingQueue<>(10);
        this.incomingQueue = new ArrayBlockingQueue<>(20);

        comm = new CommunicationObject();
        comm.initCommunication(myInfo);

        inQueueMonitor = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    while(incomingQueue.isEmpty()) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                        } finally {
                            lock.unlock();
                        }
                    }
                    send(incomingQueue.poll());
                }

            }
        });
    }

    public void send(Message message){
        if(message.getType().equals("connect")) {
            message.setVectorClock(vectorClock);
        } else {
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

    public Message getNextIncommingMessage() {
        return incomingQueue.poll();
    }
}

