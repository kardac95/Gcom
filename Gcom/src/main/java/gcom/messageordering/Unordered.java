package gcom.messageordering;

import gcom.Message;
import gcom.groupmanagement.Member;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Unordered extends Order {

    public Unordered(String myId) {
        super(myId);
    }

    @Override
    public Message sendOrder(Message message) {
        if(message.getType().equals("join")) {
            Member[] members = message.getGroup().getMembers();

            for (Member member : members) {
                if(!clock.getClock().containsKey(member.getAddress()+member.getPort())) {
                    clock.getClock().put(member.getAddress()+member.getPort(), 0L);
                }
            }
        }
        message.setVectorClock(this.clock.clone());
        return message;
    }

    @Override
    public void Ordering(Message data, Queue<Message> outQueue) {
        outQueue.add(data);
    }
}
