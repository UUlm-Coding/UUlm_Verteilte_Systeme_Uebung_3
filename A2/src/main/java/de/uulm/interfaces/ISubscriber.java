package de.uulm.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISubscriber extends Remote {
    void updateEntry(String key, String value) throws RemoteException;
    void removeEntry(String key) throws RemoteException;
}
