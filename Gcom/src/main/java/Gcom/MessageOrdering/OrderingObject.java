package Gcom.MessageOrdering;

import Gcom.GroupManagement.Member;
import Gcom.Message;

import java.util.Queue;

public class OrderingObject implements Ordering{
    Order o;
    @Override
    public void initOrdering(Member myInfo) {
        o = new Order(myInfo);
    }

    @Override
    public void addInQueue(Message message) {
        o.addNextIncomingMessage(message);
    }

    @Override
    public Message getOutQueue() {
        return o.getNextOutgoingMessage();
    }
}
