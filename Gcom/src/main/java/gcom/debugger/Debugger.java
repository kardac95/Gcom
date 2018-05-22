package gcom.debugger;

import gcom.Message;
import gcom.communication.Communication;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Debugger {
    private CopyOnWriteArrayList <Message> debugBuffer;
    private Thread debugMonitor;
    private AtomicBoolean debug;
    private AtomicBoolean play;
    private Communication comm;
    private BlockingQueue<Message> deliverQueue;

    private Lock bufferLock;
    private Condition bufferCond;
    private AtomicBoolean bufferStateChanged;

    public Debugger(Communication comm) {
        this.comm = comm;
        this.deliverQueue = new LinkedBlockingQueue<>();
        this.debug = new AtomicBoolean(false);
        this.play = new AtomicBoolean(false);
        this.bufferLock = new ReentrantLock();
        bufferStateChanged = new AtomicBoolean(false);
        bufferCond = bufferLock.newCondition();
        debugBuffer = new CopyOnWriteArrayList<>();
        debugMonitor = initDebugMonitor();
        debugMonitor.start();
    }

    private Thread initDebugMonitor(){
        return new Thread(()-> {
            while (true) {
                Message m = comm.getNextMessage();
                if (debug.get()) {
                    addDebugBuffer(m);
                    if (play.get()) {
                        debugBuffer.forEach(message -> {
                            deliverQueue.add(message);
                            removeDebugBuffer(m);
                        });
                    }
                } else {
                    deliverQueue.add(m);
                }
            }
        });
    }
    // move to gui!
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
                new Thread(updateFunction).start();
                bufferStateChanged.set(false);
                bufferLock.unlock();
            }
        });
    }

    private void addDebugBuffer(Message m) {
        bufferLock.lock();
        debugBuffer.add(m);
        bufferStateChanged.set(true);
        bufferCond.signal();
        bufferLock.unlock();
    }

    private void removeDebugBuffer(Message m) {
        bufferLock.lock();
        debugBuffer.add(m);
        bufferStateChanged.set(true);
        bufferCond.signal();
        bufferLock.unlock();
    }

    public void removeMessage(int i) {
        debugBuffer.remove(i);
    }

    public void moveMessage(int i, int j) {
        Collections.swap(debugBuffer, i,  j);
    }

    public void step() {

        if(!debugBuffer.isEmpty()) {
            //put the first element in a deliver queue.
            //remove the first element from the list.
            bufferLock.lock();
            deliverQueue.add(debugBuffer.remove(0));
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

    public List getDebugBuffer() {
        return debugBuffer;
    }

    public void setPlay(boolean play) {
        this.play.set(play);
    }

    public void setDebug(boolean debug) {
        this.debug.set(debug);
    }
}
