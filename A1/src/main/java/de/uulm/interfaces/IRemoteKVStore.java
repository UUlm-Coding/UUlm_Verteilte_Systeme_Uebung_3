package de.uulm.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteKVStore extends Remote {
    String readRemote(String key) throws RemoteException;
    void writeRemote(String key, String value) throws RemoteException;
    void removeRemote(String key) throws RemoteException;
}
