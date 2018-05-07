package Gcom.communication.rmi;

import Gcom.Message;
import java.rmi.*;


public interface RemoteObject extends Remote {
    public boolean printMessage(String msg) throws RemoteException;
    public boolean sendMessage(Message m) throws RemoteException;
}
