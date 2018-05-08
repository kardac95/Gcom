package Gcom.communication;

import Gcom.GroupManagement.Member;
import Gcom.Message;
import Gcom.communication.rmi.Node;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommunicationObject implements Communication {

    private Node n;
    private Member myInfo;

    @Override
    public void initCommunication(Member myInfo) {
        this.myInfo = myInfo;
        n = new Node(Integer.parseInt(myInfo.getPort()));
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
