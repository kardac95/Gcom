package gcom.messageordering;

import gcom.groupmanagement.Member;
import gcom.Message;

public class OrderingObject implements Ordering{
    ModuleCommunication o;
    @Override
    public void initOrdering(Member myInfo) {
        o = new ModuleCommunication(myInfo);
    }

    @Override
    public void addInQueue(Message message) {
        o.addNextIncomingMessage(message);
    }

    @Override
    public Message getOutMessage() throws InterruptedException {
        return o.getNextOutgoingMessage();
    }

    @Override
    public boolean outQueueIsEmpty() {
        return o.outQueueIsEmpty();
    }
}
