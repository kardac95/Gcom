package gcom.debugger;

import gcom.Message;
import gcom.communication.Communication;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Debugger {
    private CopyOnWriteArrayList <Message> debugBuffer;
    private Thread debugMonitor;
    private AtomicBoolean debug;
    private AtomicBoolean play;
    private Communication comm;
    private BlockingQueue<Message> deliverQueue;

    public Debugger(Communication comm) {
        this.comm = comm;
        this.deliverQueue = new LinkedBlockingQueue<>();
        debug = new AtomicBoolean(false);
        debugBuffer = new CopyOnWriteArrayList<>();
        debugMonitor = initDebugMonitor();
        debugMonitor.start();
    }

    private Thread initDebugMonitor(){
        return new Thread(()-> {
            /*Message m = null;
            try {
                m = ((LinkedBlockingQueue<Message>)comm.getInQueue()).take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
            Message m = comm.getNextMessage();
            if(debug.get()) {
                debugBuffer.add(m);
                if(play.get()) {
                    debugBuffer.forEach(message -> {
                        deliverQueue.add(message);
                        debugBuffer.remove(message);
                    });
                }

            } else {
                deliverQueue.add(m);
            }
        });
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
            deliverQueue.add(debugBuffer.remove(0));
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

    public void setPlay(boolean play) {
        this.play.set(play);
    }

    public void setDebug(boolean debug) {
        this.debug.set(debug);
    }
}
