package gcom.messageordering;

import gcom.debugger.Debug;
import gcom.groupmanagement.Member;
import gcom.Message;

public interface Ordering {
    public void initOrdering(Member myInfo);
    public void addInQueue(Message message);
    public Message getOutMessage() throws InterruptedException;
    public Debug getDebug();
    public boolean outQueueIsEmpty();
}
