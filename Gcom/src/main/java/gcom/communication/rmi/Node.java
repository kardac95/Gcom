package gcom.communication.rmi;

import gcom.groupmanagement.Member;
import gcom.Message;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node {
    private Thread server;
    private Map<String, RemoteObject> connections;
    private Registry registry;
    private Queue<Message> inQueue;

    public Node(Member myInfo) {
        connections = new HashMap<>();
        inQueue = new LinkedBlockingQueue<>();
        server = new Thread(() -> initServer(myInfo));
        server.start();
    }

    private void initServer(Member myInfo) {
        try {
            registry = LocateRegistry.createRegistry(Integer.parseInt(myInfo.getPort()));
            RemoteMessageService impl = new RemoteMessageService();
            registry.rebind("MessageService", impl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        connectToNode(myInfo);
    }

    public void unReliableUnicast(Message message, Member m) {
        try {
            connections.get(m.getAddress() + m.getPort()).sendMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unReliableMulticast(Message message, Member[] members) {
        Arrays.stream(members).forEach(m -> {
            unReliableUnicast(message, m);
        });
    }

    public void connectToNode(Member member) {
        try {
            if(connections.get(member.getAddress()+member.getPort()) == null) {
                Registry registry = LocateRegistry.getRegistry(member.getAddress(), Integer.parseInt(member.getPort()));
                RemoteObject stub = (RemoteObject) registry.lookup("MessageService");
                connections.put(member.getAddress()+member.getPort(), stub);
            }else {
                System.out.println("Connection already established");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    public void connectToNodes(Member[] members) {
        Arrays.stream(members).forEach(member -> {
            connectToNode(member);
        });
    }


    public Queue<Message> getInQueue() {
        return inQueue;
    }

    private class RemoteMessageService extends UnicastRemoteObject implements RemoteObject {
        public RemoteMessageService() throws RemoteException {
            super();
        }

        @Override
        public boolean printMessage(String msg) {
            System.out.println(msg);
            return true;
        }

        @Override
        public boolean sendMessage(Message message) throws RemoteException {
            System.out.println("Received Message");
            inQueue.add(message);
            return true;
        }

    }
}