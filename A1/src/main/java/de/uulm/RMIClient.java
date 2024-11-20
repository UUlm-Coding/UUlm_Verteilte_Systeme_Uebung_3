package de.uulm;

import de.uulm.interfaces.IRemoteKVStore;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private IRemoteKVStore kvStore;

    public RMIClient(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            kvStore = (IRemoteKVStore) registry.lookup("RemoteKVStore");
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
    }

    public void write(String key, String value) {
        try {
            kvStore.writeRemote(key, value);
            System.out.println("Written: " + key + " -> " + value);
        } catch (Exception e) {
            System.err.println("Write exception: " + e);
        }
    }

    public String read(String key) {
        try {
            return kvStore.readRemote(key);
        } catch (Exception e) {
            System.err.println("Read exception: " + e);
            return null;
        }
    }

    public void remove(String key) {
        try {
            kvStore.removeRemote(key);
            System.out.println("Removed: " + key);
        } catch (Exception e) {
            System.err.println("Remove exception: " + e.getMessage());
        }
    }
}
