package Gcom.communication;

import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.rmi.Node;

import java.util.Queue;

public class CommunicationObject implements Communication {

    Node n;
    Member myInfo;

    @Override
    public void initCommunication(Member member) {
        this.myInfo = myInfo;
        n = new Node(Integer.parseInt(myInfo.getPort()));
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
        return null;
    }
}
