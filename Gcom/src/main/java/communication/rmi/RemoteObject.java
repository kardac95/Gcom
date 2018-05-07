package communication.rmi;

import MessageOrdering.Message;

import java.rmi.*;
import java.rmi.server.*;


public interface RemoteObject extends Remote {
    public boolean printMessage(String msg) throws RemoteException;
    public boolean sendMessage(Message m) throws RemoteException;
}
