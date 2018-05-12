package gcom.communication;

import gcom.groupmanagement.Member;
import gcom.Message;
import gcom.communication.rmi.Node;

import java.util.Queue;

public class CommunicationObject implements Communication {

    private Node n;
    private Member myInfo;

    @Override
    public void initCommunication(Member myInfo) {
        this.myInfo = myInfo;
        n = new Node(myInfo);
        //n.connectToNode(myInfo);
    }

    @Override
    public void connectToMember(Member member) {
        n.connectToNode(member);
    }

    @Override
    public void connectToMembers(Member[] members) {
        n.connectToNodes(members);
    }

    @Override
    public void unReliableUnicast(Message message, Member recipient) {
        n.unReliableUnicast(message, recipient);
    }

    @Override
    public void unReliableMulticast(Message message, Member[] recipients) {
        n.unReliableMulticast(message, recipients);
    }

    @Override
    public Queue<Message> getInQueue() {
        return n.getInQueue();
    }

}
