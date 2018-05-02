package communication.rmi;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Server {
    public static void main(String args[]) {
        try {
            RemoteObject impl = new RemoteObjectImpl();
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind("MessageService", impl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
