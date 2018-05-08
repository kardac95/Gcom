package Gcom.MessageOrdering;

import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.Communication;
import Gcom.communication.CommunicationObject;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
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




    public Order(Member myInfo) {
        this.vectorClock = new ArrayList<>();
        this.memberIndex = 0;
        vectorClock.add(memberIndex);

        this.incomingQueue = new ArrayBlockingQueue<>(20);

        this.comm = new CommunicationObject();
        this.comm.initCommunication(myInfo);
        this.outgoingQueue = comm.getInQueue();

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

    protected boolean outQueueIsEmpty() {
        return outgoingQueue.isEmpty();
    }

    public Message getNextIncommingMessage() {
        return incomingQueue.poll();
    }
}

