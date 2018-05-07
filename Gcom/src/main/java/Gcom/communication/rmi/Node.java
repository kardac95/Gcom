package Gcom.communication.rmi;

import Gcom.GroupManagement.Member;
import Gcom.Message;

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

    public Node(int myPort) {
        connections = new HashMap<>();
        inQueue = new LinkedBlockingQueue<>();
        server = new Thread(new Runnable() {
            @Override
            public void run() {
                initServer(myPort);
            }
        });
        server.start();
    }

    private void initServer(int port) {
        try {
            registry = LocateRegistry.createRegistry(port);
            RemoteMessageService impl = new RemoteMessageService();
            registry.rebind("MessageService", impl);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message, Integer port) {
        try {
            connections.get(port).printMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public void unReliableBroadcast(String message) {
        connections.forEach((k,v) -> {
            try {
                v.printMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void unReliableMulticast(String message, Integer ports[]) {
        Arrays.stream(ports).forEach(p -> {
            try {
                connections.get(p).printMessage(message);
            } catch (RemoteException e) {
                //e.printStackTrace();
                System.err.println("hej");
            }
        });
    }

    public void unReliableMulticast(Message message, Member[] members) {
        Arrays.stream(members).forEach(m -> {
            try {
                connections.get(m.getAddress() + m.getPort()).sendMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
