package gcom.messageordering;

import gcom.Message;
import gcom.groupmanagement.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Order {

    VectorClock clock;
    Long lampCl;
    List <Message> buffer;

    public Order() {

    }

    public Order (String myId) {
        clock = new VectorClock(myId);
        lampCl = 0L;
        buffer = Collections.synchronizedList(new ArrayList<Message>());
    }

    public long getLampCl() {
        return lampCl;
    }

    public void incLampCl() {
        lampCl++;
    }

    public Order (List<Message> buffer) {
        this.buffer = buffer;
    }

    public abstract Message sendOrder(Message message);

    public abstract void Ordering(Message data, Queue<Message> outQueue);

    public CopyOnWriteArrayList <Message> getBuffer() {
        return new CopyOnWriteArrayList<>(buffer);
    }

    public VectorClock getClock() {
        return clock;
    }

    public void removeClockIndex(Member member) {
        this.clock.remove(member.getAddress()+member.getPort());
    }
}
