package gcom.communication;

import gcom.groupmanagement.Member;
import gcom.Message;

import java.util.Queue;

public interface Communication {
    public void initCommunication(Member myInfo);
    public void connectToMember(Member member);
    public void connectToMembers(Member[] members);
    public void unReliableUnicast(Message message, Member recipient);
    public void unReliableMulticast(Message message, Member[] recipients);
    public Queue<Message> getInQueue();
    public Message getNextMessage();
}
