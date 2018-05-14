package gcom.messageordering;

import gcom.Message;

import java.util.List;
import java.util.Queue;

public class CausalOrder extends Order {

    public CausalOrder(String myId) {
        super(myId);
    }

    @Override
    public void Ordering(Message data, Queue<Message> outQueue) {

    }
}
