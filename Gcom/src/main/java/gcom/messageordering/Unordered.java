package gcom.messageordering;

import gcom.Message;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Unordered extends Order {

    public Unordered(String myId) {
        super(myId);
    }

    @Override
    public Message sendOrder(Message message) {
        message.setVectorClock(this.clock.clone());
        return message;
    }

    @Override
    public void Ordering(Message data, Queue<Message> outQueue) {
        outQueue.add(data);
    }
}
