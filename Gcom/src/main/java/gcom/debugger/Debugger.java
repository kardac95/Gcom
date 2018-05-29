package gcom.debugger;

import gcom.Message;
import gcom.communication.Communication;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Debugger {
    private Thread debugMonitor;
    private AtomicBoolean debug;
    private Communication comm;
    private BlockingQueue<Message> deliverQueue;
    private ConcurrentHashMap<String, CopyOnWriteArrayList<Message>> groupBuffer;
    private ConcurrentHashMap<String, List<Message>> orderBuffer;

    private Lock bufferLock;
    private Condition bufferCond;
    private AtomicBoolean bufferStateChanged;

    public Debugger(Communication comm) {
        this.comm = comm;
        this.deliverQueue = new LinkedBlockingQueue<>();
        this.debug = new AtomicBoolean(false);
        this.bufferLock = new ReentrantLock();
        this.groupBuffer = new ConcurrentHashMap<>();
        this.bufferStateChanged = new AtomicBoolean(false);
        this.bufferCond = bufferLock.newCondition();
        this.debugMonitor = initDebugMonitor();
        this.debugMonitor.start();
    }

    private Thread initDebugMonitor(){
        return new Thread(()-> {
            while (true) {
                Message m = comm.getNextMessage();
                if (debug.get()) {
                    if(groupBuffer.get(m.getGroup().getName()) == null) {
                        groupBuffer.put(m.getGroup().getName(), new CopyOnWriteArrayList<>());
                    }
                    addDebugBuffer(m.getGroup().getName(), m);
                } else {
                    deliverQueue.add(m);
                }
            }
        });
    }

    public Thread monitorDebugBuffer(Runnable updateFunction) {
        return new Thread(() -> {
            while(true) {
                bufferLock.lock();
            while( !bufferStateChanged.get() ) {
                try {
                    bufferCond.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                //update Gui!
                System.err.println("UPDATE GUI");
                new Thread(updateFunction).start();
                bufferStateChanged.set(false);
                bufferLock.unlock();
            }
        });
    }

    private void addDebugBuffer(String groupName, Message m) {
        bufferLock.lock();
        groupBuffer.get(groupName).add(m);
        bufferStateChanged.set(true);
        bufferCond.signal();
        bufferLock.unlock();
    }

    public void removeMessage(String group, int i) {
        bufferLock.lock();
        groupBuffer.get(group).remove(i);
        bufferStateChanged.set(true);
        bufferCond.signal();
        bufferLock.unlock();

    }

    public void moveMessage(String group, int i, int j) {
        Collections.swap(groupBuffer.get(group), i,  j);
    }

    public void step(String group) {

        if(!groupBuffer.get(group).isEmpty()) {
            bufferLock.lock();
            deliverQueue.add(groupBuffer.get(group).remove(0));
            bufferStateChanged.set(true);
            bufferCond.signal();
            bufferLock.unlock();
        }

    }

    public Message getNextMessage() {
        Message m = null;
        try {
            m = deliverQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m;
    }

    public List getDebugBuffer(String group) {
        return groupBuffer.get(group);
    }

    public void play(String group) {
        groupBuffer.get(group).forEach(message -> {
            bufferLock.lock();
            deliverQueue.add(groupBuffer.get(group).remove(0));
            bufferStateChanged.set(true);
            bufferCond.signal();
            bufferLock.unlock();
        });
        this.debug.set(false);
    }

    public void setDebug(boolean debug) {
        this.debug.set(debug);
    }

    public List<Message> getOrderBuffer(String groupName) {
        return orderBuffer.get(groupName);
    }

    public void setOrderBuffer(String groupName, List buffer) {
        this.orderBuffer.put(groupName, buffer);
    }
}
