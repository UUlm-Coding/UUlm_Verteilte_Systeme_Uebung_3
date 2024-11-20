package de.uulm;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        RMIKVStore rmikvStore;
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            rmikvStore = new RMIKVStore();
            registry.rebind("RemoteKVStore", rmikvStore);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        RMIClient client = new RMIClient("localhost", 1099);

        System.out.println("Starting RMI Key-Value Store operations...");

        client.write("key1", "value1");
        client.write("key2", "value2");

        System.out.println("Reading key1: " + client.read("key1"));
        System.out.println("Reading key2: " + client.read("key2"));

        System.out.println("Removing key1...");
        client.remove("key1");

        System.out.println("Trying to read removed key1...");
        client.read("key1");

        System.out.println("Finished RMI Key-Value Store operations.");

        try {
            UnicastRemoteObject.unexportObject(rmikvStore, true);
            System.out.println("RMI Server stopped.");
        } catch (RemoteException e) {
            System.err.println("Error stopping RMI Server: " + e);
        }
    }
}