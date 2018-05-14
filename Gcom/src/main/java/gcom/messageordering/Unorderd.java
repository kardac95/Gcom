package gcom.messageordering;

import gcom.Message;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Unorderd extends Order {

    @Override
    public void Ordering(Message data, Queue<Message> outQueue) {
        outQueue.add(data);
    }
}
