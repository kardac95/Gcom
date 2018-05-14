package gcom.messageordering;

import gcom.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Order {

    VectorClock clock;
    List <Message> buffer;

    public Order() {

    }

    public Order (String myId) {
        clock = new VectorClock(myId);
        buffer = new ArrayList<>();
    }

    public Order (List<Message> buffer) {
        this.buffer = buffer;
    }

    public abstract void Ordering(Message data, Queue<Message> outQueue);

    public List <Message> getBuffer() {
        return buffer;
    }

    public VectorClock getClock() {
        return clock;
    }
}