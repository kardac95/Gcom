package Gcom.MessageOrdering;

import Gcom.GroupManagement.Member;
import Gcom.Message;

import java.util.Queue;

public interface Ordering {
    public void initOrdering(Member myInfo);
    public void addInQueue(Message message);
    public Message getOutMessage();
    public boolean outQueueIsEmpty();
}
