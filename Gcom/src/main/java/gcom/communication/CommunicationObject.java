package gcom.communication;

import gcom.groupmanagement.Member;
import gcom.Message;
import gcom.communication.rmi.Node;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommunicationObject implements Communication {

    private Node n;
    private Member myInfo;

    @Override
    public void initCommunication(Member myInfo) {
        this.myInfo = myInfo;
        n = new Node(myInfo);
    }

    @Override
    public void connectToMember(Member member) {
        n.connectToNode(member);
    }

    @Override
    public void connectToMembers(Member[] members) {
        n.connectToNodes(members);
    }

    public void disconnectMember(Member member) {
        n.disconnectFromNode(member);
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

    @Override
    public Message getNextMessage() {
        System.out.println("getting the goddamn call...");
        Message m = null;
        try {
            m = ((LinkedBlockingQueue<Message>)n.getInQueue()).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("12314");
        }
        System.out.println(m.getType());
        System.out.println("delivering shit");
        return m;
    }

}
