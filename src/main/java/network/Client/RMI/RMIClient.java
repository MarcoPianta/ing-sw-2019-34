package network.Client.RMI;

import network.Server.RMI.RemoteObjectInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private RMIClient client;

    public static void main (String args[]) {

        try{
            Registry registry = LocateRegistry.getRegistry(10001);
            RemoteObjectInterface stub = (RemoteObjectInterface) registry.lookup("STUB");
            String response = stub.sayHello();
            System.err.println("response: " + response);
        } catch(Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
