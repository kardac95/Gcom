package communication.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private Thread server;
    private Map<Integer, RemoteObject> connections;
    private Registry registry;

    public Node(int myPort) {
        connections = new HashMap<>();
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
            RemoteObject impl = new RemoteObjectImpl();
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

    public void connectToNode(int port) {
        try {
            if(connections.get(port) == null) {
                Registry registry = LocateRegistry.getRegistry(port);
                RemoteObject stub = (RemoteObject) registry.lookup("MessageService");
                connections.put(port, stub);
            }else {
                System.out.println("Connection already established");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

}
