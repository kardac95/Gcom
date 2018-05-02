package communication.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Node {
    private Thread server;
    private RemoteObject remote;
    private Object buffer;
    private Registry registry;

    public Node(int myPort, String HostName) {
        try {
            registry = LocateRegistry.getRegistry("localhost");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        server = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    private void initServer() {

    }

    private void serverFunction() {

    }

    public void connectToServer(int port) {

    }

}
