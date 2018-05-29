package gcom.messageordering;

import gcom.Message;
import gcom.groupmanagement.Member;

import java.util.*;

public class CausalOrder extends Order {

    public CausalOrder(String myId) {
        super(myId);
    }

    @Override
    public void Ordering(Message message, Queue<Message> outQueue) {
        if(message.getType().equals("message")) {
            if(isNext(message)) {
                outQueue.add(message);
                this.clock.updateVectorClock(message.getVectorClock());
                checkBuffer(outQueue);
            } else if((message.getSender().getAddress()+message.getSender().getPort()).equals(clock.getMyId()) && message.getVectorClock().isBefore(this.clock)) {
                outQueue.add(message);
            } else {
                this.buffer.add(message);
                sortBuffer();
            }
        } else {
            if(message.getType().equals("disconnect")) {
                removeClockIndex(message.getSender());
            }
            outQueue.add(message);
        }
    }

    public Message sendOrder(Message message) {
        switch(message.getType()) {
            case "join":
                Member[] members = message.getGroup().getMembers();

                for (Member member : members) {
                    if(!clock.getClock().containsKey(member.getAddress()+member.getPort())) {
                        clock.getClock().put(member.getAddress()+member.getPort(), 0L);
                    }
                }
                break;
            case "message":
                incLampCl();
                break;
            case "connect":
                break;
            case "disconnect":
                break;
            default:
                System.err.println("Illegal message type.");
                break;
        }
        message.setVectorClock(clock.clone());
        System.err.println("b4 " + clock.getClock());
        message.getVectorClock().getClock().put(clock.getMyId(), getLampCl());
        System.err.println("after " + clock.getClock());
        return message;
    }

    private void checkBuffer(Queue<Message> outQueue) {
        while(!this.buffer.isEmpty()) {
            Message first = this.buffer.get(0);
            if(isNext(first)) {
                outQueue.add(first);
                this.clock.updateVectorClock(first.getVectorClock());
                this.buffer.remove(0);
            } else {
                break;
            }
        }
    }

    private boolean isNext(Message message) {
        Set<String> keySet = message.getVectorClock().getClock().keySet();
        for (String key : keySet) {
            if (key.equals(message.getSender().getAddress() + message.getSender().getPort())) {
                if (!(message.getVectorClock().getValue(key) == (this.clock.getValue(key) + 1))) {
                    return false;
                }
            } else {
                if (!(message.getVectorClock().getValue(key) <= (this.clock.getValue(key)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void sortBuffer() {
        buffer.sort((m1, m2) -> (m1.getVectorClock().isBefore(m2.getVectorClock()) ? -1:1));
    }
}