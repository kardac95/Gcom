package communication.rmi;

import java.rmi.*;
import java.rmi.server.*;


public interface RemoteObject extends Remote {
    public boolean printMessage(String msg) throws RemoteException;
}
