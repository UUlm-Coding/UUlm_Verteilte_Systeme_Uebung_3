package de.uulm.interfaces;


import java.rmi.RemoteException;

public interface ISubscribeKVStore extends IRemoteKVStore {
    void subscribe(String key, ISubscriber sub) throws RemoteException;
    void unsubscribe(String key, ISubscriber sub) throws RemoteException;
}
