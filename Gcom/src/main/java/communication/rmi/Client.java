package communication.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;

public class Client {
    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");

            RemoteObject stub = (RemoteObject)registry.lookup("MessageService");

            System.out.println("Result: "+stub.printMessage("bingo"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToServer(int port) {

    }
}
